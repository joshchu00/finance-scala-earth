package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Created by joshchu999 on 12/14/16.
  */
class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

    def index = Action {
      Ok("Hello World!")
    }


//  implicit val system = ActorSystem("StockActorSystem")
//  implicit val materializer = ActorMaterializer()
//
//  import scala.concurrent.ExecutionContext.Implicits.global
//
//  val flowGetNASDAQList = (start: String) => {
//
//    val wsClient = StandaloneAhcWSClient()
//
//    wsClient.url("http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download")
//      .get()
//      .map(
//        response =>
//          response.body
//      )
//      .andThen {
//        case Success(body) =>
//          wsClient.close()
//          body
//        case Failure(e) =>
//          wsClient.close()
//          e.toString()
//      }
//  }
//
//  val flowGetSplit = (body: String) => {
//    body.split("\n").toList
//  }
//
//  val flowGetStock = (exchange: String) => (line: String) => {
//    val token = line.split(",")
//    Stock(exchange, token(0).replace("\"", ""))
//  }
//
//  val sinkNoop = Sink.ignore
//
//
//
//
//  def handleRequest(input: Request, context: Context): Response = {
//
//    val helloWorldStream: RunnableGraph[Future[Done]] =
//      Source.single("")
//        .mapAsync(1)(flowGetNASDAQList)
//        .mapConcat(flowGetSplit)
//        .map(flowGetStock("NASDAQ"))
//        .map(StockDAO.putStock)
//        .toMat(Sink.ignore)(Keep.right)
//
//    import scala.language.postfixOps
//    import scala.concurrent.duration._
//    Await.result(helloWorldStream.run(), 230 seconds)
//
//    return new Response("Go Serverless v1.0! Your function executed successfully!", input)
//  }
}
