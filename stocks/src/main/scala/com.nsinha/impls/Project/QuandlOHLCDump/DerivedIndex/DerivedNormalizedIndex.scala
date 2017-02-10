package com.nsinha.impls.Project.QuandlOHLCDump.DerivedIndex

import java.io.File

import com.nsinha.data.Csv.{Percent, Price, Volume}
import com.nsinha.utils.{FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization._

/** Created by nishchaysinha on 11/15/16.
  */
object DerivedNormalizedIndex extends Loggable {

  implicit val formats = DefaultFormats
  def processDirectory(dir : String, outputdirIn : String = "", listOfTickersInIndex : List[String], index : String) : Unit = {
    logger.info(index)
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outputdir = if (outputdirIn == "") dir else outputdirIn
    val allTickerFiles : List[File] = srcDir.listFiles().toList filter (x ⇒
      listOfTickersInIndex.contains(x.getName))
    FileUtils.createDirIfNotPresent(outputdir)

    val allTickersListOfListRows = allTickerFiles map { parse(_).extract[List[GenCsvQuoteRowScottrade]] }
    if (allTickersListOfListRows.length == 0) return

    val x = for (i ← Range(0, allTickersListOfListRows.head.length)) yield {
      val relevantRows = allTickersListOfListRows map (x ⇒ if (i >= x.length) x(x.length - 1) else x(i))
      val size = relevantRows.size
      val head = relevantRows.head
      val volume = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.volume.value
      } / size
      val high = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.highprice.value
      } / size
      val low = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.lowprice.value
      } / size
      val open = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.startprice.value
      } / size
      val close = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.endprice.value
      } / size
      val prev = relevantRows.foldLeft(0.0){ (z, el) ⇒
        z + el.prevprice.value
      } / size
      GenCsvQuoteRowScottrade(datetimeStart = head.datetimeStart, datetimeEnd = head.datetimeEnd, symbol = index, prevprice = Price(prev),
        endprice = Price(close), startprice = Price(open), highprice = Price(high), lowprice = Price(low), volume = Volume(volume), companyname = index, percentagechange = Percent(0))
    }
    val derivedIndexQuotes : List[GenCsvQuoteRowScottrade] = x toList
    val destFile = outputdir+"/"+index
    FileUtils.writeFile(destFile, writePretty(derivedIndexQuotes))
  }

}
