package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import java.io.File

import com.nsinha.impls.Project.JsonCsvProject.{ConcatenateJsonUtils, JsonUtils}
import com.nsinha.impls.Project.TimeSeries.{ClosingPriceTimeSeries, OpenPriceTimeSeries}
import com.nsinha.utils.{FileUtils, Loggable}

/**
  * Created by nishchaysinha on 10/21/16.
  */
object ClosingPriceRecords extends  YearlyRecordsTrait with Loggable {
  def clazz(x: String, admitTickers: List[String]) = new ClosingPriceTimeSeries(x,admitTickers = admitTickers)
  override def processYears(years: List[Int], jsonFileNameRootDir: String = "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",
                   inputFileRelative: String = "output/combinedData.json",outputFileRelative: String
                   = "output/closingprice.json", admitTickers: List[String]) = {
    years map { year => (new YearlyRecords) (jsonFileNameRootDir + s"/$year/$inputFileRelative",
      jsonFileNameRootDir + s"/$year/$outputFileRelative",admitTickers, clazz) }
  }

  override def processDirectory(dir: String, outFileDirRelative: String = "output/closingprice", admitTickers: List[String]) = {
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outDir = dir + "/" + outFileDirRelative
    val allFilesToConcat: List[File] = srcDir.listFiles().toList
    val allJsonStrings = for (f <- allFilesToConcat if ((f.isDirectory == false) && !f.getName.contains("last"))) yield {
      val inputFile = f.getAbsolutePath
      logger.info(inputFile)
      val (jsonStr, csvStr) = (new YearlyRecords) (inputFile, outDir + "/tmp", admitTickers, clazz)
      jsonStr
    }

    logger.info("json concatenating step")
    val jsonStr = ConcatenateJsonUtils.concatenateJsonString(allJsonStrings)
    val jsonFileName = outDir + "/" + "closingprice.json"
    JsonUtils.writeToFile( jsonFileName, jsonStr)
    JsonUtils.writeCsvFile(jsonFileName)
  }

  def main(args: Array[String]) {
    processDirectory("/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/2015", admitTickers = Nil)
  }
}









