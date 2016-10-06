package com.nsinha.impls.Project

import java.io.{File, FileInputStream, FileWriter}

import com.nsinha.impls.Project.timeSeries.{PriceNormalizedTimeSeries, ClosingPriceTimeSeries, ConsumeAllQuotes}
import com.nsinha.data.Csv.Price
import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProjectImpl
import com.nsinha.impls.Project.Orders.CsvOrderScottradeProjectImpl
import com.nsinha.impls.Project.Quotes.CsvDailyQuotesScottradeProjectImpl
import com.nsinha.impls.Project.YearlyQuoteAnalysisProject.YearlyQuoteAnalysisProjectImpl
import com.nsinha.utils.{FileUtils, Loggable, StringUtils}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.scalatest.{FunSuite, ShouldMatchers}
import scaldi.Injectable
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.io.Source

class CsvImplVer1Test extends FunSuite with  ShouldMatchers with Injectable with Loggable {

  test("test1") {
    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", csvFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/file_datestart05302016T16:00:00Zdateend.csv", classzz = GenCsvQuoteRowScottrade.getClass)
    quoteImpl.appendDataToYearFile("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/metafile2016.txt","/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val topFlowers = quoteImpl.writeTopFlowersForToday(10)
    topFlowers.toString() should be ("List(GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,CORE,Price(45.11),Price(38.72),Price(44.15),Price(44.15),Price(37.16),Volume(3083014.0),CORE MARK HOLDING COMPANY INCORPORATED,Percent(14.17)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,EDIT,Price(18.33),Price(17.42),Price(18.16),Price(18.2329),Price(16.56),Volume(398374.0),EDITAS MEDICINE INCORPORATED,Percent(4.96)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,GFI,Price(5.8),Price(5.55),Price(5.66),Price(5.69),Price(5.53),Volume(5713647.0),GOLD FIELDS LTD,Percent(4.31)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,CXW,Price(17.51),Price(16.79),Price(17.68),Price(18.14),Price(15.07),Volume(1.1253477E7),CORRECTIONS CORPORATION OF AMERICA,Percent(4.11)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,VIRT,Price(17.4),Price(16.8),Price(17.4),Price(17.4),Price(16.78),Volume(534489.0),VIRTU FINANCIAL INC,Percent(3.45)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,BRSS,Price(29.68),Price(28.75),Price(29.63),Price(29.94),Price(28.61),Volume(133365.0),GLOBAL BRASS AND COPPER HOLDINGS INC,Percent(3.13)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,FDML,Price(9.29),Price(9.04),Price(9.37),Price(9.4068),Price(8.93),Volume(179206.0),FEDERALMOGUL HOLDINGS CORPORATION,Percent(2.69)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,SCTY,Price(22.05),Price(21.46),Price(22.12),Price(22.2),Price(21.41),Volume(2432962.0),SOLARCITY CORPORATION,Percent(2.68)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,OCANF,Price(3.43),Price(3.34),Price(3.37),Price(3.43),Price(3.33),Volume(70150.0),OCEANAGOLD CORPORATION,Percent(2.62)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,ISIL,Price(19.13),Price(18.65),Price(19.0),Price(19.13),Price(18.63),Volume(2290542.0),INTERSIL CORPORATION,Percent(2.51)))")
    logger.info(topFlowers.toString())
  }

  test("test2") {
    val csvFile = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/Securities_to_Watch2016.10.05.15.50.22datestart10052016T16:00:00Zdateend.csv"
    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", csvFilePath = csvFile, classzz = GenCsvQuoteRowScottrade.getClass)
    val orderImpl = new CsvOrderScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt",dumpFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/CompletedOrders_datestart09272016T16:00:00Zdateend.csv", quoteImpl)
    orderImpl.dumpPerformanceCurrentHolds("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/test")
    orderImpl.dumpPerformanceCurrentHoldsGroupedOnSymbol("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/testCombine")
  }

  test("testYearlyTS"){
    val analysisProject = new YearlyQuoteAnalysisProjectImpl("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val str = analysisProject.createTimeSeries[Price](axisString = "endprice", canBuildT =  new Price(0))
    logger.info(str)

  }

  test("csvtest1") {
    val jsonFileName = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/test"
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    println(str)
    fw.write(str)
    fw.close()
  }

  test("csvtest2") {
    val jsonFileName = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/testCombine"
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    println(str)
    fw.write(str)
    fw.close()
  }

  test("process all quotes") {
      val consumeAllQuotes = new ConsumeAllQuotes("/Users/nishchaysinha/stocksdatadir/currentPerformance")
      consumeAllQuotes.consumeTheWholeDirectoryAndMoveToProcessed
  }

  test("process closing price") {
    val clpTsClazz = new PriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = clpTsClazz.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/closingpricenormalized.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }
}
