package com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly

import com.nsinha.impls.Project.TimeSeries.ClosingPriceTimeSeries

/**
  * Created by nishchaysinha on 10/21/16.
  */
object VolumeRecords {
  def clazz(x: String, admitTickers: List[String]) = new ClosingPriceTimeSeries(x,admitTickers = admitTickers)
  def processYears(years: List[Int], jsonFileNameRootDir: String = "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",
                   inputFileRelative: String = "output/combinedData.json",outputFileRelative: String
                   = "output/volume.json", admitTickers: List[String]) = {
    years map { year => (new YearlyRecords) (year, jsonFileNameRootDir + s"/$year/$inputFileRelative",
      jsonFileNameRootDir + s"/$year/$outputFileRelative",admitTickers, clazz) }
  }
}
