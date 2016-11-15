package com.nsinha.apps.TimeSeriesApps.IndexClosings

import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly.ClosingPriceRecords

/**
  * Created by nishchaysinha on 11/15/16.
  */
object ClosingPriceIndexes {
  val INDEXES = List ("DJI", "RUSSELL", "STOXX", "BSE", "NIKKEI", "HANSENG", "NASDAQ", "SP500", "DAX")


  def run() = {
    ClosingPriceRecords.processYears(Range(1990, 2017).toList, outputFileRelative = "output/indexesclosingprice.json",
      admitTickers = INDEXES)
  }

  def main(args: Array[String]) {
    run()
  }

}
