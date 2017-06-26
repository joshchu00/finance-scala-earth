package idv.trashchu.finance.crawler

import com.google.inject.AbstractModule
import idv.trashchu.finance.crawler.services.CrawlerService

/**
  * Created by joshchu999 on 5/4/17.
  */
class Module extends AbstractModule {

  def configure() = {
    bind(classOf[CrawlerService]).asEagerSingleton()
  }
}
