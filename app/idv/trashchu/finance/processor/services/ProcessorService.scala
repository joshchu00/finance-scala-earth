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
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

import scala.concurrent.duration._

/**
  * Created by joshchu999 on 6/8/17.
  */
@Singleton
class ProcessorService @Inject()(configuration: Configuration, implicit val system: ActorSystem) {

  val logger: Logger = Logger(this.getClass())

  val kafkaTopicsProcessorConfiguration = configuration.getString("finance.earth.kafka.topics.processor").getOrElse("")

  logger.debug(s"topicProcessor = $kafkaTopicsProcessorConfiguration")


  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

  implicit val materializer = ActorMaterializer()

  Consumer.committableSource(consumerSettings, Subscriptions.topics(kafkaTopicsProcessorConfiguration))
    .map { message =>
      logger.debug(message.record.value)
      message.committableOffset.commitScaladsl()
    }
    .runWith(Sink.ignore)

}
