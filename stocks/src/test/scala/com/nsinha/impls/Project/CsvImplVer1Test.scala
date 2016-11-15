package com.nsinha.impls.Project

import java.io.{File, FileInputStream, FileWriter}

import com.nsinha.apps.OrdersApps.OrderAnalysis
import com.nsinha.impls.Project.TimeSeries._
import com.nsinha.data.Csv.Price
import com.nsinha.impls.Project.JsonCsvProject.{ConcatenateJsonFiles, JsonCsvProjectImpl, JsonUtils}
import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly._
import com.nsinha.impls.Project.Quotes.Scottrade.{ConsumeAllScottradeQuotes, CsvDailyQuotesScottradeProjectImpl}
import com.nsinha.impls.Project.YearlyQuoteAnalysisProject.YearlyQuoteAnalysisProjectImpl
import com.nsinha.utils.{FileUtils, Loggable, StringUtils}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.scalatest.{FunSuite, ShouldMatchers}
import JsonUtils._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration
import scala.concurrent.duration.Duration
import scala.concurrent.duration.{FiniteDuration, SECONDS}
import scaldi.Injectable
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.concurrent.Await
import scala.io.Source

class CsvImplVer1Test extends FunSuite with  ShouldMatchers with Injectable with Loggable {






  test("csvtest2") {
    val jsonFileName = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/testCombine"
    writeCsvFile(jsonFileName)

  }

  test("testYearlyTS"){
    val analysisProject = new YearlyQuoteAnalysisProjectImpl("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val str = analysisProject.createTimeSeries[Price](axisString = "endprice", canBuildT =  new Price(0))
    logger.info(str)
  }

  test("process closing price") {
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/closingprice.json"
    val clpTsClazz = new ClosingPriceTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = clpTsClazz.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    FileUtils.writeFile(jsonFileName, jsonStr)
    writeCsvFile(jsonFileName)
  }

  test("process normalized closing price with filtering") {
    val clpTsObject = new ClosingPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = clpTsObject.getTransformed
    val ts_winnersFor15days  = PriceNormalizedTimeSeries.getTheWinnersPastIntervals(ts, 15)
    val ts_winnersFor30days  = PriceNormalizedTimeSeries.getTheWinnersPastIntervals(ts, 30)

    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/closingpricenormalized.json"
    val jsonFileName15days = jsonFileName + ".15days.json"
    val jsonFileName30days = jsonFileName + ".30days.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    FileUtils.writeFile(jsonFileName15days, writePretty(ts_winnersFor15days))
    FileUtils.writeFile(jsonFileName30days, writePretty(ts_winnersFor30days) )
    writeCsvFile(jsonFileName)
    writeCsvFile(jsonFileName15days)
    writeCsvFile(jsonFileName30days)
  }





  test("low price") {
    val cl_lp_TsObject = new LowPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.get
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowprice.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

  test("low price normalized") {
    val cl_lp_TsObject = new LowPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowpricenormalized.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

  test("low price to closing normalized") {
    val cl_lp_TsObject = new LowPriceToClosingNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowpricetoclosingpricenormalized.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

}
