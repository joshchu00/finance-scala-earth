package idv.trashchu.finance.crawler.taifex.option.services

import java.time._
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import idv.trashchu.finance.crawler.Crawler
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext

/**
  * Created by joshchu00 on 5/4/17.
  */
@Singleton
class TaifexOptionRecordCrawlerService @Inject()(configuration: Configuration, implicit val system: ActorSystem, implicit val ec: ExecutionContext, ws: WSClient) extends Crawler {

  // logger
  override val logger: Logger = Logger(this.getClass())

  // configuration
  val environmentConfiguration = configuration.get[String]("finance.environment")
  logger.debug(s"environment = $environmentConfiguration")

  val kafkaTopicsProcessorConfiguration = configuration.get[String]("finance.kafka.topics.processor")
  logger.debug(s"topicProcessor = $kafkaTopicsProcessorConfiguration")

  val dataBaseDirectoryConfiguration = configuration.get[String]("finance.earth.data.base.directory")
  logger.debug(s"dataBaseDirectory = $dataBaseDirectoryConfiguration")

  // actor
  implicit val materializer = ActorMaterializer()

  override val actorSystem = system
  override val actorMaterializer = materializer
  override val executionContext = ec

  // zone
  override val timezone = "+8"

  // tick
  val nowDateTime: LocalDateTime = LocalDateTime.now(Clock.system(ZoneId.of(timezone)))
  val startDateTime: LocalDateTime = nowDateTime plusHours 1 withMinute 3 withSecond 0 withNano 0

  val duration: Duration = Duration.between(nowDateTime.toInstant(ZoneOffset.of(timezone)), startDateTime.toInstant(ZoneOffset.of(timezone)))

  override val initialDelay = environmentConfiguration match {
    case "dev" | "test" => scala.concurrent.duration.Duration(1, scala.concurrent.duration.MILLISECONDS)
    case "stg" | "prod" => scala.concurrent.duration.Duration.fromNanos(duration.toNanos)
    case _ => null
  }
  override val interval = environmentConfiguration match {
    case "dev" | "test" => scala.concurrent.duration.Duration(10, scala.concurrent.duration.SECONDS)
    case "stg" | "prod" => scala.concurrent.duration.Duration(1, scala.concurrent.duration.HOURS)
    case _ => null
  }

  // input
  override val wsClient = ws
  override val url = "http://www.taifex.com.tw/chinese/3/3_2_2.asp"
  override def formData(dateTime: LocalDateTime) = {

    val year: Int = dateTime.getYear
    val month: Int = dateTime.getMonthValue
    val day: Int = dateTime.getDayOfMonth

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
  }

  // filter
  override def filter(dateTime: LocalDateTime) = {

    val hour: Int = dateTime.getHour

    environmentConfiguration match {
      case "dev" | "test" => true
      case "stg" | "prod" => hour >= 14 && hour <= 18
      case _ => true
    }
  }

  // output
  override val kafkaTopicsProcessor = kafkaTopicsProcessorConfiguration
  override def dataFilePath(dateTime: LocalDateTime): String = {

    val year: Int = dateTime.getYear
    val month: Int = dateTime.getMonthValue
    val day: Int = dateTime.getDayOfMonth

    f"$dataBaseDirectoryConfiguration/taifex/optionRecord/$year%04d$month%02d$day%02d.html"
  }











  crawl

}
