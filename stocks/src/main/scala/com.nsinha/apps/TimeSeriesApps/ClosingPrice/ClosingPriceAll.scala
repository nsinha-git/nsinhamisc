package com.nsinha.apps.TimeSeriesApps.ClosingPrice

import com.nsinha.impls.Project.QuandlOHLCDump.NormalizedOHLC.NormalizeTickers
import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly.{ClosingPriceRecords, HighPriceRecords, LowPriceRecords}

/**
  * Created by nishchaysinha on 11/15/16.
  */
object ClosingPriceAll {
  def run() = {
    // override def processDoir(inputFile: String, outFileDir: String = "output/closingprice", admitTickers: List[String])
    for (year <- Range(2016, 2017)) {
      println(year)
      ClosingPriceRecords.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}/normalized",  admitTickers = Nil)
    }
  }

  def main(args: Array[String]) {
    run()
  }
}

object HighPriceAll {
  def run() = {
    // override def processDoir(inputFile: String, outFileDir: String = "output/closingprice", admitTickers: List[String])
    for (year <- Range(2000, 2017)) {
      println(year)
      HighPriceRecords.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}/normalized",  admitTickers = Nil)
    }
  }

  def main(args: Array[String]) {
    run()
  }
}


object LowPriceAll {
  def run() = {
    // override def processDoir(inputFile: String, outFileDir: String = "output/closingprice", admitTickers: List[String])
    for (year <- Range(2000, 2017)) {
      println(year)
      LowPriceRecords.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}/normalized",  admitTickers = Nil)
    }
  }

  def main(args: Array[String]) {
    run()
  }
}
