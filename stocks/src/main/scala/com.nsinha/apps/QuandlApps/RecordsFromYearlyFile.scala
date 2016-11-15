package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly._

/**
  * Created by nishchaysinha on 11/14/16.
  */
object RecordsFromYearlyFile {
  def run() = {
    val srcDir ="/Users/nishchaysinha/stocksdatadir/ohlc/yearlies"
    //ClosingPriceRecords.processYears(Range(1985, 2017).toList, srcDir)
    //OpenPriceRecords.processYears(Range(1985, 2017).toList, srcDir)
    //LowPriceRecords.processYears(Range(1985, 2017).toList, srcDir)
    //HighPriceRecords.processYears(Range(1985, 2017).toList, srcDir)
    //VolumeRecords.processYears(Range(1985, 2017).toList, srcDir)
  }

  def main(args: Array[String]) {
    run()
  }

}
