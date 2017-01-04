package com.nsinha.impls.Project.QuandlOHLCDump

/**
  * Created by nishchaysinha on 10/9/16.
  */
import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nsinha.data.Csv.{Percent, Price, Volume}
import org.joda.time.DateTime
import spray.http.{HttpMethods, HttpRequest, HttpResponse, Uri}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty
import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import DateTimeUtils._
import scala.concurrent.duration.Duration
import scala.collection.convert.WrapAsScala._



object QuoteForTickerFromQuandl extends QuoteForTickerFromQuandlTrait with Loggable {

  implicit val system = ActorSystem()
  implicit val timeout: Timeout = 1000000


  private def getHistoricalQuoteMapInt(ticker: String, url: String ): Future[HttpResponse] = {
    import system.dispatcher // execution context for futures
    val httpRequest: HttpRequest = new HttpRequest(method = HttpMethods.GET, uri = Uri(url))
    (IO(Http) ? httpRequest).mapTo[HttpResponse]

  }
  private def getHistoricalQuoteInt(ticker: String, urlPrefix: String ): Future[HttpResponse] = {
    import system.dispatcher // execution context for futures
    val httpRequest: HttpRequest = new HttpRequest(method = HttpMethods.GET, uri = Uri(s"$urlPrefix/${ticker}.json?api_key=Xmky6espzDoofkY9CFar"))
    (IO(Http) ? httpRequest).mapTo[HttpResponse]

  }

  private def getHistoricalQuoteListOfRows(ticker: String, httpResponse: HttpResponse, latest: Long): List[GenCsvQuoteRowScottrade] = {
    val jsonByteArray: Array[Byte] = httpResponse.entity.data.toByteArray
    val rootNode = mapper.readTree(jsonByteArray).path("dataset")
    val cols: JsonNode = rootNode.path("column_names")
    val dataArray = rootNode.path("data")
    val colMapper :Map[String, String] = getColMapForData(cols)
    val listQuotes : List[GenCsvQuoteRowScottrade] = convertToQuotesArray(ticker, dataArray, mapper = colMapper, fn = marshalToJsonObject)
    logger.info(s"$ticker ${listQuotes.length}")
    listQuotes filter(x => x.datetimeStart > latest)
  }




  private def convertToQuotesArray(ticker: String, dataArray: JsonNode, mapper: Map[String, String] = Map(DATE->"index 0", "datetimeend"-> "index 0", HIGH-> "index 2"
  , LOW -> "index 3" , OPEN -> "index 1", CLOSE -> "index 4", "volume" -> "index 5", PREV -> "index 1" ), fn: (String, JsonNode, Map[String, String]) => GenCsvQuoteRowScottrade): List[GenCsvQuoteRowScottrade] = {
    val allElems: List[JsonNode] = dataArray.elements() toList
    var accumulator: List[GenCsvQuoteRowScottrade] = List()
    for (elem <- allElems) {
      val x = fn(ticker, elem, mapper)
      accumulator = accumulator :+ (x)
    }
    accumulator
  }

  private def marshalToJsonObject(ticker: String, elem: JsonNode, mapper: Map[String, String]): GenCsvQuoteRowScottrade = {
    if (elem.getNodeType == JsonNodeType.ARRAY) {
      val (dateTimeStart: Long, dateTimeEnd: Long) = {
        val index = mapper(DATE).replaceAll("index", "").replaceAll(" ", "").toInt
        val dt = parseDateTime(elem.get(index).asText(), format = "yyyy-MM-dd hh:mm:ss a")
        (toStartBusinessHourOfDay(dt).getMillis, toEndBusinessHourOfDay(dt).getMillis)
      }

      val lowprice: Double = {
        val index = mapper(LOW).replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val highprice: Double = {
        val index = mapper(HIGH).replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val startprice: Double = {
        val index = mapper(OPEN).replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val endprice: Double = {
        val index = mapper(CLOSE).replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val prevprice: Double = {
        val index = mapper(PREV).replaceAll("index", "").replaceAll(" ", "").toInt
        elem.get(index).asDouble()
      }
      val volume: Double = {
        val index = mapper.get(VOLUME) map {_.replaceAll("index", "").replaceAll(" ", "").toInt}
        index map {x => elem.get(x).asDouble()} getOrElse (0.0)
      }
      GenCsvQuoteRowScottrade(dateTimeStart, dateTimeEnd, ticker,Price(prevprice),Price(endprice), Price(startprice),
        Price(highprice), Price(lowprice), Volume(volume), companyname = ticker, Percent(0))
    } else {
      ???
      null
    }
  }

  def getHistoricalQuoteForTicker(ticker: String, urlPrefix: String, latest: Long): Future[List[GenCsvQuoteRowScottrade]] = {
    val fut = getHistoricalQuoteInt(ticker, urlPrefix)
    fut  map (httpResponse =>  getHistoricalQuoteListOfRows(ticker, httpResponse, latest))
  }

  def getHistoricalQuoteMap(ticker: String, url: String, latest: Long): Future[List[GenCsvQuoteRowScottrade]] = {
    val fut = getHistoricalQuoteMapInt(ticker, url)
    fut  map (httpResponse =>  getHistoricalQuoteListOfRows(ticker, httpResponse, latest))
  }

  override def createYearlyFilesForMaps(ticker: String, dir: String, url: String, latest: Long): Future[Unit] = {
    logger.info(ticker)
    val listOfQuotesFut = getHistoricalQuoteMap(ticker, url, latest)
    val l = listOfQuotesFut map (listOfQuotes => divideIntoYearlyBins(ticker, listOfQuotes)) map (x => writeYearlyFilesFromYearlyQuotes(ticker,x,dir))
    l
  }

  override def createYearlyFilesForTicker(ticker: String, dir: String, urlPrefix: String, latest: Long): Future[Unit] = {
    logger.info(ticker)
    val listOfQuotesFut = getHistoricalQuoteForTicker(ticker, urlPrefix, latest)
    val l = listOfQuotesFut map (listOfQuotes => divideIntoYearlyBins(ticker, listOfQuotes)) map (x => writeYearlyFilesFromYearlyQuotes(ticker,x,dir))
    l
  }




}
