package com.nsinha.impls.Project.QuandlOHLCDump

import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nsinha.data.Csv.{Percent, Price, Volume}
import spray.http.{HttpMethods, HttpRequest, HttpResponse, Uri}
import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import DateTimeUtils._

import scala.collection.convert.WrapAsScala._
import scala.concurrent.Future

/**
  * Created by nishchaysinha on 11/15/16.
  */
object QuoteForBondFromQuandl extends QuoteForBondsFromQuandlTrait {
  implicit val system = ActorSystem()
  implicit val timeout: Timeout = 1000000



  private def getHistoricalQuoteMapInt(ticker: String, url: String ): Future[HttpResponse] = {
    import system.dispatcher // execution context for futures
    val httpRequest: HttpRequest = new HttpRequest(method = HttpMethods.GET, uri = Uri(url))
    (IO(Http) ? httpRequest).mapTo[HttpResponse]

  }

  private def getHistoricalQuoteListOfRows(ticker: String, httpResponse: HttpResponse, latest: Long): Map[String,List[GenCsvQuoteRowScottrade]] = {
    val jsonByteArray: Array[Byte] = httpResponse.entity.data.toByteArray
    val rootNode = mapper.readTree(jsonByteArray).path("dataset")
    val cols: JsonNode = rootNode.path("column_names")
    val dataArray = rootNode.path("data")
    val (tickerIndexMap, colsMap) = getBondsMapForData(ticker, cols)
    var mapOfList: Map[String, List[GenCsvQuoteRowScottrade]] = Map()
    for (tickerIndex <- tickerIndexMap) {

      val listQuotes : List[GenCsvQuoteRowScottrade] = convertToQuotesArray(tickerIndex._1, dataArray, tickerIndex._2, colsMap, fn = marshalToJsonObject)  filter(x => x.datetimeStart > latest)
      mapOfList = mapOfList + (tickerIndex._1 -> listQuotes)
    }
    mapOfList
  }

  private def convertToQuotesArray(bond: String, dataArray: JsonNode, index: Int, colsMap: Map[String, Int], fn: (String, JsonNode, Int, Map[String, Int]) => GenCsvQuoteRowScottrade ): List[GenCsvQuoteRowScottrade] = {
    val allElems: List[JsonNode] = dataArray.elements() toList

    var accumulator: List[GenCsvQuoteRowScottrade] = List()
    for (elem <- allElems) {
      val x = fn(bond, elem, index, colsMap)
      accumulator = accumulator :+ (x)
    }
    accumulator

  }



  private def marshalToJsonObject(bond: String, elem: JsonNode, dataIndex: Int, colMap: Map[String, Int]): GenCsvQuoteRowScottrade = {
    if (elem.getNodeType == JsonNodeType.ARRAY) {
      val (dateTimeStart: Long, dateTimeEnd: Long) = {
        val index = colMap(DATE)
        val dt = parseDateTime(elem.get(index).asText(), format = "yyyy-MM-dd hh:mm:ss a")
        (toStartBusinessHourOfDay(dt).getMillis, toEndBusinessHourOfDay(dt).getMillis)
      }

      val lowprice: Double = {
        val index = dataIndex
        elem.get(index).asDouble()
      }
      val highprice: Double = {
        val index = dataIndex
        elem.get(index).asDouble()
      }
      val startprice: Double = {
        val index = dataIndex
        elem.get(index).asDouble()
      }
      val endprice: Double = {
        val index = dataIndex
        elem.get(index).asDouble()
      }
      val prevprice: Double = {
        val index = dataIndex
        elem.get(index).asDouble()
      }
      val volume: Double = {
        0.0
      }
      GenCsvQuoteRowScottrade(dateTimeStart, dateTimeEnd, bond,Price(prevprice),Price(endprice), Price(startprice),
        Price(highprice), Price(lowprice), Volume(volume), companyname = bond, Percent(0))
    } else {
      ???
      null
    }
  }


  override def getHistoricalQuoteMap(ticker: String, url: String, latest: Long): Future[Map[String, List[GenCsvQuoteRowScottrade]]] =  {
    val fut = getHistoricalQuoteMapInt(ticker, url)
    fut  map (httpResponse =>  getHistoricalQuoteListOfRows(ticker, httpResponse, latest))
  }

  override def createYearlyFilesForMaps(bond: String, dir: String, url: String, latest: Long): Future[Unit] = {
    logger.info(bond)

    val mapOflistOfQuotesFut = getHistoricalQuoteMap(bond, url, latest)

    mapOflistOfQuotesFut map (mp => {
      mp map { kv =>
        val x = divideIntoYearlyBins(kv._1, kv._2)
        writeYearlyFilesFromYearlyQuotes(kv._1, x, dir)
      }
    })
  }
}
