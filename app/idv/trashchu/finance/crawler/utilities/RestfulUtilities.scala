package idv.trashchu.finance.crawler.utilities

import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by joshchu00 on 5/4/17.
  */
object RestfulUtilities {

  val logger: Logger = Logger(this.getClass())


  def get(ws: WSClient, url: String): Future[String] = {

    logger.debug(s"GET : $url")

    ws.url(url)
      .get()
      .map(
        _.body
      )
  }

  def post(ws: WSClient, url: String, formData: Map[String, Seq[String]]): Future[String] = {

    logger.debug(s"POST : $url")

    ws.url(url)
      .post(formData)
      .map(
        _.body
      )
  }
}
