package idv.trashchu.finance.processor.services

import java.nio.file.Paths
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import idv.trashchu.finance.crawler.utilities.{FileUtilities, RestfulUtilities}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by joshchu00 on 6/8/17.
  */
@Singleton
class ProcessorService @Inject()(configuration: Configuration, implicit val system: ActorSystem, implicit val ec: ExecutionContext) {

  val logger: Logger = Logger(this.getClass())

  val kafkaTopicsProcessorConfiguration = configuration.get[String]("finance.kafka.topics.processor")

  logger.debug(s"topicProcessor = $kafkaTopicsProcessorConfiguration")


  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

  implicit val materializer = ActorMaterializer()

  Consumer.committableSource(consumerSettings, Subscriptions.topics(kafkaTopicsProcessorConfiguration))
    .mapAsync(1) { message =>

      logger.debug(s"ProcessorService: ${message.record.value}")

      this.processOptionRecordFile(message.record.value)
        .map { _ =>
          message.committableOffset.commitScaladsl()
        }
    }
    .runWith(Sink.ignore)

  private def processOptionRecordFile(dataFilePath: String) = {

    logger.debug(s"processOptionRecordFile: $dataFilePath")

    FileIO.fromPath(Paths.get(dataFilePath))
      .fold(ByteString()) {
        _ ++ _
      }
      .mapConcat { data =>

        val html = data.utf8String

//        logger.debug(s"$html")

        Jsoup.parse(html)
          .getElementsByClass("table_c")
          .get(0)
          .select("tr[bgcolor=ivory]")
          .toArray
          .map { case tr: Element =>

            tr.select("td")
              .toArray
              .map { case td: Element =>

                td
              }
          }
          .toList
      }
      .runWith(Sink.ignore)
  }

}
