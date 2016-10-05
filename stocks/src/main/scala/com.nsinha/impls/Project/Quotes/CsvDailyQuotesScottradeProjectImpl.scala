package com.nsinha.impls.Project.Quotes

import java.io.{File, FileWriter}
import java.nio.file.{FileSystem, FileSystems}

import com.nsinha.data.Csv._
import com.nsinha.data.Project.CsvDailyQuotesScottradeProject
import com.nsinha.utils._
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.io.Source


class CsvDailyQuotesScottradeProjectImpl(modelFilePath: String, csvFilePath: String, classzz: Class[_]) extends CsvDailyQuotesScottradeProject with Loggable {
  val modelFile = new File(modelFilePath)
  val csvFile = new File(csvFilePath)
  var startDateTime: DateTime = null
  var endDateTime: DateTime = null
  val csvModel = readModelMap(modelFile)
  val rows: List[GenCsvQuoteRowScottrade]  = readSingleDayGroupedQuotesCsv(csvFile, csvModel, classzz)

  def readModelMap(file: File): CsvModel = {
    val s: Source = Source.fromFile(file)
    val symbol = "Symbol"
    val companyName = "Company Name"
    val low = "Low"
    val last = "Last Price"
    val open  = "Open"
    val high  = "High"
    val prevClose  = "Prev Close"
    val change = "% Chg"
    val volume = "Volume"
    val map = Map(last -> "endprice", open -> "startprice", high -> "highprice",
      low -> "lowprice", prevClose -> "prevclose", change -> "percentchange", volume -> "volume",
      companyName -> "companyname", symbol -> "symbol" )
    CsvModel(map)
  }

  override def getQuoteForToday(symbol: String, `type`: String): Price = {
    {rows filter (r => r.symbol == symbol)}.head.endprice
  }

  override def readSingleDayGroupedQuotesCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[GenCsvQuoteRowScottrade] = {
    val source = FileUtils.openACsvFile(file)
    val prefix = file.getName().split("\\.")(0)
    val (startDateTime_, endDateTime_) = DateTimeUtils.parseDate(file)
    startDateTime = startDateTime_
    endDateTime = endDateTime_
    var i = 0
    var start = false
    var cols: Map[String, Int] = null
    var result: List[GenCsvQuoteRowScottrade] = List()
    for (line <- source.getLines()) {
      if(cols == null ||  cols == Nil) {
        cols = createColsList(StringUtils.extractPrintable(line), csvModel, prefix)
      } else {
          val rowCols: Map[String, String] =  extractRowCols(line.replaceAll("\\p{Cntrl}", ""), cols)
          result = result.+:(mapToCsvRow(rowCols, prefix))
      }
    }
    source.close()
    result
  }

  override def appendDataToYearFile(metaDataYearFile: String,  yearFile: String): Unit = {
    val source = FileUtils.openOrCreateFile(metaDataYearFile)
    var seen = false
    for (line <- source.getLines()) {
      if(line.contains(s"${startDateTime.getMillis},${endDateTime.getMillis}")) { seen = true}
    }
    source.close()
    if (seen == false) {
      appendToAggregateAnalysisFile(yearFile, rows)
      val fw = new FileWriter(metaDataYearFile,true)
      fw.append(s"${startDateTime.getMillis},${endDateTime.getMillis}\n")
      fw.flush()
      fw.close
    }
  }

  override def writeTopVolumesForToday(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("volume", true) )
    rows.take(i)
  }

  override def writeTopGainersForToday(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("percent", true) )
    rows.take(i)
  }

  override def writeTopLoosersForToday(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("percent", false) )
    rows.take(i)
  }

  override def writeTopFlowersForToday(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("flow", true) )
    rows.take(i)
  }

  override def extractWatchersForToday(file: File): List[GenCsvQuoteRowScottrade]= {
    implicit val format = DefaultFormats
    val src: Source = FileUtils.openOrCreateFile(file)
    val allLines = src.getLines()
    var watchers: Seq[String] = Seq()
    src.close()
    for (line <- allLines){
      watchers = watchers.+:(line)
    }
    val watchedRows = rows.filter( x => {
      if (watchers.contains(x.getKey)) {
        true
      } else {
        false
      }
    })
    watchedRows
  }

  override def appendToAggregateAnalysisFile[A >: CsvQuoteRow](fileName: String, csvrows: List[A]): Unit = {
    implicit val format = DefaultFormats
    val json = writePretty(csvrows)
    JsonUtils.appendToAJsonFile(fileName, json)
  }

  def mapToCsvRow(rowCols: Map[String,String], prefix: String): GenCsvQuoteRowScottrade = {
    GenCsvQuoteRowScottrade(startDateTime.getMillis, endDateTime.getMillis, symbol = rowCols("symbol"), prevprice = Price(rowCols("prevclose")), endprice = Price(rowCols("endprice")),
     startprice = Price(rowCols("startprice")), highprice = Price(rowCols("highprice")), lowprice = Price(rowCols("lowprice")),
     volume = Volume(rowCols("volume")), companyname = rowCols("companyname"), percentagechange = Percent(rowCols("percentchange"))
   )
 }
}
