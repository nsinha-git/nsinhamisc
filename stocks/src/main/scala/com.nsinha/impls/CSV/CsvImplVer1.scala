package main.scala.com.nsinha.impls.CSV

import java.io.{File, FileOutputStream, FileWriter}
import java.util.Date

import com.nsinha.utils.{DateTimeUtils, Loggable}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty
import org.json4s.jackson.JsonMethods._
import org.joda.time.DateTime
import main.scala.com.nsinha.data.CSV.generated.GenCsvRows

import scala.io.Source
import main.scala.com.nsinha.data.CSV._


class CsvImplVer1(modelFilePath: String, csvFilePath: String, classzz: Class[_]) extends Csv with Loggable {
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


  override def readCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[GenCsvRows] = {
    val source = Source.fromFile(file, "UTF-8")
    val prefix = file.getName().split("\\.")(0)
    parseDate(file)
    var i = 0
    var start = false
    var cols: Map[String, Int] = null
    var result: List[GenCsvRows] = List()
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

  override def writeTopVolume(i: Int): List[GenCsvRows] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvRows.sort("volume", true) )
    rows.take(i)
  }

  override def writeTopGainers(i: Int): List[GenCsvRows] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvRows.sort("percent", true) )
    rows.take(i)
  }
  override def writeTopLoosers(i: Int): List[GenCsvRows] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvRows.sort("percent", false) )
    rows.take(i)
  }

  override def writeTopFlowers(i: Int): List[GenCsvRows] = {
    implicit val format = DefaultFormats
    rows.sortWith(GenCsvRows.sort("flow", true) )
    rows.take(i)
  }

  override def extractWatchers (file: File): List[GenCsvRows]= {
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

  override def appendToAggregateAnalysisFile[A >: CsvRow](file: File, csvrows: List[A]): Unit = {
    implicit val format = DefaultFormats
    val json = writePretty(csvrows)
    val fileWriter = new FileWriter(file)
    fileWriter.append(json)
    fileWriter.close()
  }

  private def createColsList(line: String, csvModel: CsvModel, prefix: String): Map[String, Int] = {
    val preModelCols = line.split(",").map(_.trim)
    val cols: Map[String, Int] = mapModel(csvModel, preModelCols,prefix)
    cols
  }

  private def mapModel(csvModel: CsvModel, preModelCols: Array[String], prefix: String): Map[String, Int]= {
    var map : Map[String,Int] = Map()
    var i = 0
    val modelMap = csvModel.map
    for (preModelCol <- preModelCols) {
      val mappedCol = modelMap.get(preModelCol.toString)
      mappedCol match {
        case None =>
        case Some(x) =>
          map = map + ( s"$x" ->i)
      }
      i = i + 1
    }
    map
  }

  private def encode(line: String) : String = {
    val splitsAtColon = line.split("\"")
    var ll: String = ""
    for (i <- Range(0,splitsAtColon.length)) {
      val x =i%2 match {
        case 1 => splitsAtColon(i).replaceAll(",","")
        case  _ => splitsAtColon(i)
      }
      ll = ll + x
    }
    ll.replaceAll("\\+","").replaceAll("-","").replaceAll("%","")
  }

  private def extractRowCols(line: String, cols: Map[String, Int]): Map[String, String] = {
    val allCols = encode(line).split(",")
    var res :Map[String, String]= Map()
    for (x <- cols.keys){
      val y = allCols(cols(x))
      res = res + (x->y)
    }
    res
  }

 private def mapToCsvRow(rowCols: Map[String,String], prefix: String): GenCsvRows = {
   GenCsvRows(startDateTime.getMillis, endDateTime.getMillis, symbol = rowCols("symbol"), prevprice = Price(rowCols("prevclose")), endprice = Price(rowCols("endprice")),
     startprice = Price(rowCols("startprice")), highprice = Price(rowCols("highprice")), lowprice = Price(rowCols("lowprice")),
     volume = Volume(rowCols("volume")), companyname = rowCols("companyname"), percentagechange = Percent(rowCols("percentchange"))
   )
 }

}
