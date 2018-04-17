package idv.trashchu.finance.crawler

import java.nio.file.Paths
import java.time.{Clock, LocalDateTime, ZoneId}

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import idv.trashchu.finance.crawler.utilities.RestfulUtilities
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

/**
  * Created by joshchu00 on 5/4/17.
  */
trait Crawler {

  // logger
  def logger: Logger

  // actor
  implicit def actorSystem: ActorSystem
  implicit def actorMaterializer: ActorMaterializer
  implicit def executionContext: ExecutionContext

  // zone
  def timezone: String

  // tick
  def initialDelay: FiniteDuration
  def interval: FiniteDuration

  // input
  def wsClient: WSClient
  def url: String
  def formData(dateTime: LocalDateTime): Map[String, Seq[String]]

  // filter
  def filter(dateTime: LocalDateTime): Boolean

  // output
  def kafkaTopicsProcessor: String
  def dataFilePath(dateTime: LocalDateTime): String




  private def getURL(dateTime: LocalDateTime) = {

    logger.debug(s"getURL: $dateTime")

    Source.single(Nil)
      .mapAsync(1){ _ =>
        RestfulUtilities.post(
          wsClient,
          url,
          formData(dateTime)
        )
      }
      .map(ByteString(_))
      .runWith(FileIO.toPath(Paths.get(dataFilePath(dateTime))))
  }

  private def flow(dateTime: LocalDateTime) = {

    logger.debug(s"flow: $dateTime")

    Source.single(Nil)
      .filter { _ =>
        filter(dateTime)
      }
      .mapAsync(1) { _ =>
        getURL(dateTime)
          .map { _ =>
            new ProducerRecord[Array[Byte], String](kafkaTopicsProcessor, dataFilePath(dateTime))
          }
      }
      .runWith(Producer.plainSink(ProducerSettings(actorSystem, new ByteArraySerializer, new StringSerializer)))
  }

  def crawl() = {

    logger.debug(s"crawl: $initialDelay $interval")

    Source.tick(
      initialDelay,
      interval,
      Nil
    )
      .mapAsync(1) { _ =>
        flow(LocalDateTime.now(Clock.system(ZoneId.of(timezone))))
      }
      .runWith(Sink.ignore)
  }
}
