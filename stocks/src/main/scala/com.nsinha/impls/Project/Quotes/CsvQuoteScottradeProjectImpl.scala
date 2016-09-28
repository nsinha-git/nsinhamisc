package com.nsinha.impls.Project.Quotes

import java.io.{File, FileWriter}

import com.nsinha.data.Csv._
import com.nsinha.data.Project.CsvQuoteScottradeProject
import com.nsinha.utils.{DateTimeUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.io.Source


class CsvQuoteScottradeProjectImpl(modelFilePath: String, csvFilePath: String, classzz: Class[_]) extends CsvQuoteScottradeProject with Loggable {
  val modelFile = new File(modelFilePath)
  val csvFile = new File(csvFilePath)
  var startDateTime: DateTime = null
  var endDateTime: DateTime = null
  val csvModel = readModelMap(modelFile)
  val rows = readCsv(csvFile, csvModel, classzz)

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

  override def getDates(file: File): List[DateTime] = {
    val re = ".*datestart(.*)dateend".r

    val matches = re.findAllMatchIn(file.getName())

    val dates: List[DateTime] = matches map { x =>
      val time = x.group(1)
      val dateTime: DateTime = DateTime.parse(time)
      dateTime
    } toList

    DateTimeUtils.sort(dates)
  }

  def parseDate(file:File) = {
    val dates = getDates(file)
    val currentDateTime = dates.head
    startDateTime = DateTimeUtils.toStartBusinessHourOfDay(currentDateTime)
    endDateTime = DateTimeUtils.toEndBusinessHourOfDay(currentDateTime)
  }


  override def readCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[GenCsvQuoteRowScottrade] = {
    val source = Source.fromFile(file, "UTF-8")
    val prefix = file.getName().split("\\.")(0)
    parseDate(file)
    var i = 0
    var start = false
    var cols: Map[String, Int] = null
    var result: List[GenCsvQuoteRowScottrade] = List()
    for (line <- source.getLines()) {
      start match {
        case false => if (line.length > 1) {
          cols = createColsList(line, csvModel, prefix)
        }
        case true =>
          val rowCols: Map[String, String] =  extractRowCols(line, cols)
          result = result.+:(mapToCsvRow(rowCols, prefix))
      }
      if (cols != null){
        start = true
      }
      i = i + 1
    }
    result
  }

  override def appendDataToYearFile(metaDataYearFile: String,  yearFile: String): Unit = {
    val source = Source.fromFile(metaDataYearFile, "UTF-8")
    var seen = false
    for (line <- source.getLines()) {
      if(line.contains(s"${startDateTime.getMillis},${endDateTime.getMillis}")) { seen = true}
    }
    if (seen == false) {
      appendToAggregateAnalysisFile(new File(yearFile), rows)
      val fw = new FileWriter(metaDataYearFile)
      fw.append(s"${startDateTime.getMillis},${endDateTime.getMillis}")
      fw.close
    }
  }

  override def writeTopVolume(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("volume", true) )
    rows.take(i)
  }

  override def writeTopGainers(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("percent", true) )
    rows.take(i)
  }
  override def writeTopLoosers(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("percent", false) )
    rows.take(i)
  }

  override def writeTopFlowers(i: Int): List[GenCsvQuoteRowScottrade] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvQuoteRowScottrade.sort("flow", true) )
    rows.take(i)
  }

  override def extractWatchers (file: File): List[GenCsvQuoteRowScottrade]= {
    implicit val format = DefaultFormats
    val src = Source.fromFile(file)
    val allLines = src.getLines()
    var watchers: Seq[String] = Seq()
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

  override def appendToAggregateAnalysisFile[A >: CsvQuoteRow](file: File, csvrows: List[A]): Unit = {
    implicit val format = DefaultFormats
    val json = writePretty(csvrows)
    val fileWriter = new FileWriter(file)
    fileWriter.append(json)
    fileWriter.close()
  }

 def mapToCsvRow(rowCols: Map[String,String], prefix: String): GenCsvQuoteRowScottrade = {
   GenCsvQuoteRowScottrade(startDateTime.getMillis, endDateTime.getMillis, symbol = rowCols("symbol"), prevprice = Price(rowCols("prevclose")), endprice = Price(rowCols("endprice")),
     startprice = Price(rowCols("startprice")), highprice = Price(rowCols("highprice")), lowprice = Price(rowCols("lowprice")),
     volume = Volume(rowCols("volume")), companyname = rowCols("companyname"), percentagechange = Percent(rowCols("percentchange"))
   )
 }

}
