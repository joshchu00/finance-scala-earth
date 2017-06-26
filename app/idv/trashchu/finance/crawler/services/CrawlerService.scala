package idv.trashchu.finance.crawler.services

import java.nio.file.Paths
import java.time.{Clock, Duration, LocalDateTime, ZoneId, ZoneOffset}
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import idv.trashchu.finance.crawler.utilities.RestfulUtilities
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

/**
  * Created by joshchu999 on 5/4/17.
  */
@Singleton
class CrawlerService @Inject() (configuration: Configuration, implicit val system: ActorSystem, implicit val ec: ExecutionContext, ws: WSClient) {

  val logger: Logger = Logger(this.getClass())

  val kafkaTopicsProcessorConfiguration = configuration.getString("finance.earth.kafka.topics.processor").getOrElse("")
  val dataBaseDirectoryConfiguration = configuration.getString("finance.earth.data.base.directory").getOrElse("")

  logger.debug(s"topicProcessor = $kafkaTopicsProcessorConfiguration, dataBase = $dataBaseDirectoryConfiguration")

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)


  implicit val materializer = ActorMaterializer()

  val nowDateTime: LocalDateTime = LocalDateTime.now(Clock.system(ZoneId.of("+8")))
  val startDateTime: LocalDateTime = nowDateTime plusHours 1 withMinute 3 withSecond 0 withNano 0
  val duration: Duration = Duration.between(nowDateTime.toInstant(ZoneOffset.of("+8")), startDateTime.toInstant(ZoneOffset.of("+8")))

  Source.tick(
    // TODO: remove, just for test
//    scala.concurrent.duration.Duration(1, scala.concurrent.duration.MILLISECONDS),
    scala.concurrent.duration.Duration.fromNanos(duration.toNanos),
    scala.concurrent.duration.Duration(1, scala.concurrent.duration.HOURS),
    (dataBaseDirectoryConfiguration, producerSettings, kafkaTopicsProcessorConfiguration)
  )
    .mapAsync(1) { case (dataBaseDirectory, producerSettings, topicsProcessor) =>
      this.crawlOptionRecordFlow(dataBaseDirectory, producerSettings, topicsProcessor)
    }
    .runWith(Sink.ignore)

  private def crawlOptionRecordFlow(dataBaseDirectory: String, producerSettings: ProducerSettings[Array[Byte], String], topicsProcessor: String) = {

    val dateTime: LocalDateTime = LocalDateTime.now(Clock.system(ZoneId.of("+8")))

    logger.debug(s"crawlOptionRecordFlow: $dateTime")

    Source.single(
      (dateTime, dataBaseDirectory, producerSettings)
    )
      .filter { case (dateTime, dataBaseDirectory, producerSettings) =>

        val hour: Int = dateTime.getHour

        hour >= 14 && hour <= 18
      }
      .mapAsync(1) { case (dateTime, dataBaseDirectory, producerSettings) =>

        val year: Int = dateTime.getYear
        val month: Int = dateTime.getMonthValue
        val day: Int = dateTime.getDayOfMonth

        val dataFilePath: String = f"$dataBaseDirectory/optionRecord/$year%04d$month%02d$day%02d.html"

        this.crawlOptionRecordURL(year, month, day, dataFilePath)
          .map { _ =>
            new ProducerRecord[Array[Byte], String](topicsProcessor, dataFilePath)
          }
      }
      .runWith(Producer.plainSink(producerSettings))
  }

  private def crawlOptionRecordURL(year: Int, month: Int, day: Int, dataFilePath: String) = {

    logger.debug(f"crawlOptionRecordURL: $year%04d$month%02d$day%02d")

    // TODO: how to pass datetime to runwith
    Source.single(Nil)
      .mapAsync(1){ _ =>
        RestfulUtilities.post(
          ws,
          "http://www.taifex.com.tw/chinese/3/3_2_2.asp",
          Map(
            "qtype" -> Seq("2"),
            "commodity_id" -> Seq("TXO"),
            "commodity_id2" -> Seq(),
            "market_code" -> Seq("0"),
            "goday" -> Seq(),
            "dateaddcnt" -> Seq("0"),
            "DATA_DATE_Y" -> Seq(s"$year"),
            "DATA_DATE_M" -> Seq(s"$month"),
            "DATA_DATE_D" -> Seq(s"$day"),
            "syear" -> Seq(s"$year"),
            "smonth" -> Seq(s"$month"),
            "sday" -> Seq(s"$day"),
            "datestart" -> Seq(s"$year%2F$month%2F$day"),
            "MarketCode" -> Seq("0"),
            "commodity_idt" -> Seq("TXO"),
            "commodity_id2t" -> Seq(),
            "commodity_id2t2" -> Seq()
          )
        )
      }
      .map(ByteString(_))
      .runWith(FileIO.toPath(Paths.get(dataFilePath)))
  }
}
