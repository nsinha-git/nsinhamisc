package com.nsinha.impls.Project.QuandlOHLCDump.NormalizedOHLC

import java.io.File

import com.nsinha.utils.{FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

/** Created by nishchaysinha on 11/15/16.
  */
object NormalizeTickers extends Loggable {
  implicit val formats = DefaultFormats

  def processDirectory(dir : String, outputdirIn : String = "") = {
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outputdir = if (outputdirIn == "") dir+"/normalized" else outputdirIn
    val allFiles : List[File] = srcDir.listFiles().toList
    FileUtils.createDirIfNotPresent(outputdir)
    for (f ← allFiles if ((f.isDirectory == false) && !f.getName.contains("last"))) {
      val destFile = outputdir+"/"+f.getName
      val jsonNode = parse(f)
      logger.info(f.getName)
      val listOfQuotes : List[GenCsvQuoteRowScottrade] = jsonNode.extract[List[GenCsvQuoteRowScottrade]]
      val normalElement : GenCsvQuoteRowScottrade = listOfQuotes.reverse.head
      val normalizedListOfQuotes = listOfQuotes map (elem ⇒ GenCsvQuoteRowScottrade.normalize(elem, normalElement))
      FileUtils.writeFile(destFile, writePretty(normalizedListOfQuotes))

    }
  }

  def main(args : Array[String]) {
    processDirectory("/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/2015/temp")
  }

}
