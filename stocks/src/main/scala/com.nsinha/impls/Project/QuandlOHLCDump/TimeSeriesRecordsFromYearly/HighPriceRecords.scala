package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import java.io.File

import com.nsinha.impls.Project.JsonCsvProject.{ConcatenateJsonUtils, JsonUtils}
import com.nsinha.impls.Project.TimeSeries.{ClosingPriceTimeSeries, HighPriceTimeSeries, OpenPriceTimeSeries}
import com.nsinha.utils.FileUtils

/** Created by nishchaysinha on 10/21/16.
  */
object HighPriceRecords extends YearlyRecordsTrait {
  def clazz(x : String, admitTickers : List[String]) = new HighPriceTimeSeries(x, admitTickers = admitTickers)
  override def processYears(years : List[Int], jsonFileNameRootDir : String = "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",
                            inputFileRelative : String = "output/combinedData.json", outputFileRelative : String = "output/highprice.json", admitTickers : List[String]) = {
    years map { year ⇒
      (new YearlyRecords) (
        jsonFileNameRootDir + s"/$year/$inputFileRelative",
        jsonFileNameRootDir + s"/$year/$outputFileRelative", admitTickers, clazz
      )
    }
  }

  override def processDirectory(dir : String, outFileDirRelative : String = "output/highprice", admitTickers : List[String]) = {
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outDir = dir+"/"+outFileDirRelative
    val allFilesToConcat : List[File] = srcDir.listFiles().toList
    val allJsonStrings = for (f ← allFilesToConcat if ((f.isDirectory == false) && !f.getName.contains("last"))) yield {
      val inputFile = f.getAbsolutePath
      val (jsonStr, csvStr) = (new YearlyRecords) (inputFile, outDir+"/tmp", admitTickers, clazz)
      jsonStr
    }
    val jsonStr = ConcatenateJsonUtils.concatenateJsonString(allJsonStrings)
    val jsonFileName = outDir+"/"+"highprice.json"
    JsonUtils.writeToFile(jsonFileName, jsonStr)
    JsonUtils.writeCsvFile(jsonFileName)
  }

  def main(args : Array[String]) {
    processDirectory("/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/2015", admitTickers = Nil)
  }
}