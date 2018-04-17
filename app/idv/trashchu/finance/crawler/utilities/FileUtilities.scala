package idv.trashchu.finance.crawler.utilities

import java.io.{BufferedWriter, File, FileWriter}

import play.api.Logger

/**
  * Created by joshchu00 on 6/7/17.
  */
object FileUtilities {

  def write(filename: String, context: String) = {

    Logger.info(s"FileUtilities: $context")

    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(context)
    bw.close()
  }
}
