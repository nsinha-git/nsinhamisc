package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import java.io.{File, FileWriter}

import com.nsinha.impls.Project.JsonCsvProject.{JsonCsvProjectImpl, JsonUtils}
import com.nsinha.impls.Project.TimeSeries.{ClosingPriceTimeSeries, TimeSeries}
import com.nsinha.utils.FileUtils
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

class YearlyRecords {
  implicit val format = DefaultFormats

  def apply(year: Int, jsonFileNameInput: String, outputFile: String, admitTickers: List[String], clazz: (String, List[String]) => TimeSeries) = {
    val clpTsClazz = clazz(jsonFileNameInput, admitTickers)
    val ts = clpTsClazz.getTransformed
    val jsonStr = writePretty(ts)
    FileUtils.writeFile(outputFile, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "", jsonFile = outputFile, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val csvFile = new File(outputFile + ".csv")
    JsonUtils.writeToFile(csvFile, str)
  }
}
object YearlyRecords {
  def processOHLCForYears(years: List[Int]) = {
    //ClosingPriceRecords.processYears(years)
    //OpenPriceRecords.processYears(years)
    //LowPriceRecords.processYears(years)
    //HighPriceRecords.processYears(years)
    //VolumeRecords.processYears(years)
  }
}


