package idv.trashchu.finance.processor

import com.google.inject.AbstractModule
import idv.trashchu.finance.processor.services.ProcessorService

/**
  * Created by joshchu00 on 6/13/17.
  */
class Module extends AbstractModule {

  def configure() = {
    bind(classOf[ProcessorService]).asEagerSingleton()
  }
}
