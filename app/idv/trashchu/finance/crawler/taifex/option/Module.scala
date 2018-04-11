package idv.trashchu.finance.crawler.taifex.option

import com.google.inject.AbstractModule
import idv.trashchu.finance.crawler.taifex.option.services.TaifexOptionRecordCrawlerService

/**
  * Created by joshchu999 on 5/4/17.
  */
class Module extends AbstractModule {

  def configure() = {
    bind(classOf[TaifexOptionRecordCrawlerService]).asEagerSingleton()
  }
}
