package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by joshchu999 on 12/14/16.
  */
class HomeController extends Controller {

    def index = Action {
      Ok("Hello World!")
    }
}
