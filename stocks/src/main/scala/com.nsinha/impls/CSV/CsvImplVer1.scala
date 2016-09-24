package main.scala.com.nsinha.impls.CSV

import java.io.{File, FileOutputStream, FileWriter}
import java.util.Date

import com.nsinha.utils.Loggable
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import org.json4s.jackson.JsonMethods._
import main.scala.com.nsinha.data.CSV.generated.GenCsvRows
import scala.io.Source
import main.scala.com.nsinha.data.CSV._


class CsvImplVer1(modelFilePath: String, csvFilePath: String, classzz: Class[_]) extends Csv with Loggable {
  val modelFile = new File(modelFilePath)
  val csvFile = new File(csvFilePath)

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
    val prev  = "Prev Close"
    val change = "% Chg"
    val volume = "Volume"
    val map = Map(last -> "endprice", open -> "startprice", high -> "highprice",
      low -> "lowprice", prev -> "prevprice", change -> "percentchange", volume -> "volume",
      companyName -> "companyname", symbol -> "symbol" )
    CsvModel(map)
  }


  override def readCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[GenCsvRows] = {
    val source = Source.fromFile(file, "UTF-8")
    file.getName
    val prefix = file.getName().split("\\.")(0)
    var i = 0
    var start = false
    var cols: Map[String, Int] = null
    var result: List[GenCsvRows] = List()
    for (line <- source.getLines()) {
      start match {
        case false => if(line.length > 1) {
          cols = createColsList(line, csvModel, prefix)
        }
        case true =>
          val rowCols: Map[String, String] =  extractRowCols(line, cols)
          result = result.+:(mapToCsvRow(rowCols,prefix))
      }
      if (cols !=null){
        start = true
      }
      i = i + 1
    }
    result
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
    val json = write(csvrows)
    val fileWriter =new FileWriter(file)
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
    ll
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

 private def mapToCsvRow(rowCols: Map[String,String],prefix: String): GenCsvRows = {
  // case class GenCsvRows (date: Date, symbol: String, endprice: Price, startprice: Price, highprice: Price, lowprice: Price,
   // volume: Volume, companyname: String, percentagechange: Percent) extends CsvRow(date)
   GenCsvRows(date = new Date(), symbol = rowCols("symbol"), endprice = Price(rowCols("endprice")),
     startprice = Price(rowCols("startprice")), highprice = Price(rowCols("highprice")), lowprice = Price(rowCols("lowprice")),
     volume = Volume(rowCols("volume")), companyname = rowCols("companyname"), percentagechange = Percent(rowCols("percentagechange"))
   )
 }

}
