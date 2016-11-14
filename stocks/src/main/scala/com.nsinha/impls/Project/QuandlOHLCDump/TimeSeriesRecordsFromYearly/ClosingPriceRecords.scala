package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import com.nsinha.impls.Project.TimeSeries.{ClosingPriceTimeSeries, OpenPriceTimeSeries}

/**
  * Created by nishchaysinha on 10/21/16.
  */
object ClosingPriceRecords{
  def clazz(x: String) = new ClosingPriceTimeSeries(x)
  def processYears(years: List[Int], jsonFileNameRootDir: String = "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",
                   inputFileRelative: String = "output/combinedData.json",outputFileRelative: String
                   = "output/closingprice.json") = {
    years map { year => (new YearlyRecords) (year, jsonFileNameRootDir + s"/$year/$inputFileRelative",
      jsonFileNameRootDir + s"/$year/$outputFileRelative",clazz) }
  }
}









