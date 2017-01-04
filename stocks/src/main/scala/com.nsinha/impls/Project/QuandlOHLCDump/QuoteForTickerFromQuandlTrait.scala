package com.nsinha.impls.Project.QuandlOHLCDump

import java.io.{File, FileReader, LineNumberReader}

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nsinha.impls.Project.JsonCsvProject.JsonUtils
import com.nsinha.utils.DateTimeUtils._
import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

import scala.collection.convert.WrapAsScala._
import scala.concurrent.Future
import scala.io.Source

/**
  * Created by nishchaysinha on 11/15/16.
  *
  *
  */

trait BaseQuandlTrait{
  val DATE = "datetimestart"
  val OPEN = "openprice"
  val CLOSE = "closeprice"
  val HIGH = "highprice"
  val LOW = "lowprice"
  val VOLUME = "volume"
  val PREV = "openprice"

  val mapper = new ObjectMapper()

  def writeYearlyFilesFromYearlyQuotes(ticker: String, yearlyQuotes: Map[String, List[GenCsvQuoteRowScottrade]], dir: String) = {
    implicit val formats = DefaultFormats
    var lastYear: String = "0"
    var lastRowDateTime : Long = 0


    for (mp <- yearlyQuotes) {
      val metaDataLastProcessedDateFileNameTemp = dir + "/" + mp._1 + "/" + ticker + "lasttimeprocessed"
      val  lastTs = {
        if (new File(metaDataLastProcessedDateFileNameTemp).exists()) {
          val src =  new LineNumberReader(new FileReader(new  File(metaDataLastProcessedDateFileNameTemp)))
          src.readLine().toLong
        } else {
          0
        }
      }
      val toAppendFile = dir + "/" + mp._1 + "/" + ticker
      JsonUtils.appendToAJsonFile(toAppendFile, writePretty(mp._2.filter(x => x.datetimeStart > lastTs)))
      lastYear = if (mp._1 > lastYear) mp._1 else lastYear
      lastRowDateTime = if (mp._2.reverse.head.datetimeStart > lastRowDateTime) mp._2.reverse.head.datetimeStart else lastRowDateTime
    }

    val metaDataLastProcessedDateFileName = dir + "/" + lastYear + "/" + ticker + "lasttimeprocessed"
    FileUtils.writeFile(metaDataLastProcessedDateFileName, lastRowDateTime.toString)

  }

  def divideIntoYearlyBins(ticker: String, quotes: List[GenCsvQuoteRowScottrade]): Map[String, List[GenCsvQuoteRowScottrade]] = {
    val yearToQuotes: List[(String , GenCsvQuoteRowScottrade)] = {quotes map  { quote =>
      val dateTime: DateTime = new DateTime(quote.datetimeStart.toLong)
      getYearInYYYY(dateTime) -> quote
    }}

    yearToQuotes.foldLeft(Map[String, List[GenCsvQuoteRowScottrade]]()) { (Z, el) =>
      Z.get(el._1) match {
        case None =>
          Z.+(el._1 -> List(el._2))
        case Some(x) =>
          Z.+ (el._1 -> (List(el._2) ++ x))
      }
    }
  }
}
trait QuoteForTickerFromQuandlTrait extends BaseQuandlTrait {

  def getHistoricalQuoteForTicker(ticker: String, urlPrefix: String, latest: Long): Future[List[GenCsvQuoteRowScottrade]]
  def getHistoricalQuoteMap(ticker: String, url: String, latest: Long): Future[List[GenCsvQuoteRowScottrade]]
  def createYearlyFilesForTicker(ticker: String, dir: String, urlPrefix: String, latest: Long): Future[Unit]
  def createYearlyFilesForMaps(ticker: String, dir: String, url: String, latest: Long): Future[Unit]


  def getColMapForData(cols: JsonNode): Map[String, String] = {
    var res = Map[String, String]()
    val listOfIndices: List[JsonNode] = cols.iterator() toList

    for ( l <- listOfIndices map (_.asText()) zip Range(0, listOfIndices.length)) {
      if (l._1.toLowerCase contains("date")) res = res + (DATE -> s"index ${l._2}")
      if (l._1.toLowerCase contains("vol")) res = res + (VOLUME -> s"index ${l._2}")
      if (l._1.toLowerCase contains("low")) res = res + (LOW -> s"index ${l._2}")
      if ((l._1.toLowerCase.contains("open")) ||  (l._1.toLowerCase contains("prev")) ) res = res ++ List(OPEN -> s"index ${l._2}", PREV -> s"index ${l._2}")
      if (l._1.toLowerCase contains("close")) res = res + (CLOSE -> s"index ${l._2}")
      if (l._1.toLowerCase contains("high")) res = res + (HIGH -> s"index ${l._2}")
    }
    res
  }


}


trait QuoteForBondsFromQuandlTrait extends  BaseQuandlTrait{


  def getHistoricalQuoteMap(ticker: String, url: String, latest: Long): Future[Map[String, List[GenCsvQuoteRowScottrade]]]
  def createYearlyFilesForMaps(ticker: String, dir: String, url: String, latest: Long): Future[Unit]


  def getBondsMapForData(prefix : String, cols: JsonNode): (Map[String, Int], Map[String, Int]) = {
    var resTicker = Map[String, Int]()
    var resCols = Map[String, Int]()
    val listOfIndices: List[JsonNode] = cols.iterator() toList

    for ( l <- listOfIndices map (_.asText()) zip Range(0, listOfIndices.length)) {
      if (l._1.contains(" ")) {
        resTicker = resTicker + ((prefix + l._1.replace(" ", "")) -> l._2)
      } else {
        if (l._1.toLowerCase contains("date")) resCols = resCols + (DATE -> l._2)
      }

    }

    (resTicker, resCols)
  }
}
