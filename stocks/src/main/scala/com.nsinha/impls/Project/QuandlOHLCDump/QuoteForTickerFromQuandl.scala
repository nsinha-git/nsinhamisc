package com.nsinha.impls.Project.QuandlOHLCDump

/**
  * Created by nishchaysinha on 10/9/16.
  */
import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future }
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import scala.collection.convert.WrapAsScala._
import com.nsinha.data.Csv.{Percent, Price, Volume}
import org.joda.time.DateTime
import spray.http.{HttpMethods, HttpRequest, HttpResponse, Uri}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty
import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import DateTimeUtils._
import scala.concurrent.duration.Duration

trait QuoteForTickerFromQuandl {
  def getHistoricalQuote(ticker: String, urlPrefix: String): Future[List[GenCsvQuoteRowScottrade]]
  def getHistoricalQuoteMap(ticker: String, url: String): Future[List[GenCsvQuoteRowScottrade]]
  def createYearlyFilesForTicker(ticker: String, dir: String, urlPrefix: String)
  def createYearlyFilesForMaps(ticker: String, dir: String, url: String)
}
object QuoteForTickerFromQuandl extends QuoteForTickerFromQuandl with Loggable {
  implicit val system = ActorSystem()
  implicit val timeout: Timeout = 1000000
  val mapper = new ObjectMapper()

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

  private def getHistoricalQuoteListOfRows(ticker: String, httpResponse: HttpResponse): List[GenCsvQuoteRowScottrade] = {
    val jsonByteArray: Array[Byte] = httpResponse.entity.data.toByteArray
    val rootNode = mapper.readTree(jsonByteArray).path("dataset")
    val cols = rootNode.path("column_names")
    val dataArray = rootNode.path("data")
    val listQuotes : List[GenCsvQuoteRowScottrade] = convertToQuotesArray(ticker, dataArray, fn = marshalToJsonObject)
    listQuotes
  }

  private def convertToQuotesArray(ticker: String, dataArray: JsonNode, mapper: Map[String, String] = Map("datetimestart"->"index 0", "datetimeend"-> "index 0", "highprice"-> "index 2"
  , "lowprice" -> "index 3" , "openprice" -> "index 1", "closeprice" -> "index 4", "volume" -> "index 5", "prevprice" -> "index 1" ), fn: (String, JsonNode, Map[String, String]) => GenCsvQuoteRowScottrade): List[GenCsvQuoteRowScottrade] = {
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
        val index = mapper("datetimestart").replaceAll("index", "").replaceAll(" ", "").toInt
        val dt = parseDateTime(elem.get(index).asText(), format = "yyyy-MM-dd hh:mm:ss a")
        (toStartBusinessHourOfDay(dt).getMillis, toEndBusinessHourOfDay(dt).getMillis)
      }

      val lowprice: Double = {
        val index = mapper("lowprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val highprice: Double = {
        val index = mapper("highprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val startprice: Double = {
        val index = mapper("openprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val endprice: Double = {
        val index = mapper("closeprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val prevprice: Double = {
        val index = mapper("prevprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      val volume: Double = {
        val index = mapper("prevprice").replaceAll("index", "").replaceAll(" ", "").toInt
         elem.get(index).asDouble()
      }
      GenCsvQuoteRowScottrade(dateTimeStart, dateTimeEnd, ticker,Price(prevprice),Price(endprice), Price(startprice),
        Price(highprice), Price(lowprice), Volume(volume), companyname = ticker, Percent(0))
    } else {
      ???
      null
    }
  }

  def getHistoricalQuote(ticker: String, urlPrefix: String): Future[List[GenCsvQuoteRowScottrade]] = {
    val fut = getHistoricalQuoteInt(ticker, urlPrefix)
    fut  map (httpResponse =>  getHistoricalQuoteListOfRows(ticker, httpResponse))
  }

  def getHistoricalQuoteMap(ticker: String, url: String): Future[List[GenCsvQuoteRowScottrade]] = {
    val fut = getHistoricalQuoteMapInt(ticker, url)
    fut  map (httpResponse =>  getHistoricalQuoteListOfRows(ticker, httpResponse))
  }

  override def createYearlyFilesForMaps(ticker: String, dir: String, url: String) = {
    logger.info(ticker)
    val listOfQuotesFut = getHistoricalQuoteMap(ticker, url)
    val l = listOfQuotesFut map (listOfQuotes => divideIntoYearlyBins(ticker, listOfQuotes)) map (x => writeYearlyFilesFromYearlyQuotes(ticker,x,dir))
    Await.result(l, Duration.Inf)
  }

  override def createYearlyFilesForTicker(ticker: String, dir: String, urlPrefix: String) = {
    logger.info(ticker)
    val listOfQuotesFut = getHistoricalQuote(ticker, urlPrefix)
    val l = listOfQuotesFut map (listOfQuotes => divideIntoYearlyBins(ticker, listOfQuotes)) map (x => writeYearlyFilesFromYearlyQuotes(ticker,x,dir))
    Await.result(l, Duration.Inf)
  }



  private def divideIntoYearlyBins(ticker: String, quotes: List[GenCsvQuoteRowScottrade]): Map[String, List[GenCsvQuoteRowScottrade]] = {
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

  private def writeYearlyFilesFromYearlyQuotes(ticker: String, yearlyQuotes: Map[String, List[GenCsvQuoteRowScottrade]], dir: String) = {
    implicit val formats = DefaultFormats
    for (mp <- yearlyQuotes) {
      FileUtils.writeFile(dir + "/" + mp._1 + "/" + ticker, writePretty(mp._2))
    }
  }
}
