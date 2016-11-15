package com.nsinha.apps.TimeSeriesApps.ClosingPrice

import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly.ClosingPriceRecords

/**
  * Created by nishchaysinha on 11/15/16.
  */
object ClosingPriceAll {
  def run() = {
    ClosingPriceRecords.processYears(Range(1990, 2017).toList, outputFileRelative = "output/closingprice.json",
      admitTickers = List())
  }

  def main(args: Array[String]) {
    run()
  }

}
