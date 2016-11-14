package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import java.io.FileWriter

import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProjectImpl
import com.nsinha.impls.Project.TimeSeries.{ClosingPriceTimeSeries, TimeSeries}
import com.nsinha.utils.FileUtils
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

class YearlyRecords {
  implicit val format = DefaultFormats

  def apply(year: Int, jsonFileNameInput: String, outputFile: String, clazz: (String) => TimeSeries) = {
    val clpTsClazz = clazz(jsonFileNameInput)
    val ts = clpTsClazz.getTransformed
    val jsonStr = writePretty(ts)
    FileUtils.writeFile(outputFile, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "", jsonFile = outputFile, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(outputFile + ".csv")
    fw.write(str)
    fw.close()
  }
}
object YearlyRecords {
  def processOHLCForYears(years: List[Int]) = {
    ClosingPriceRecords.processYears(years)
    OpenPriceRecords.processYears(years)
    LowPriceRecords.processYears(years)
    HighPriceRecords.processYears(years)
    VolumeRecords.processYears(years)
  }
}


