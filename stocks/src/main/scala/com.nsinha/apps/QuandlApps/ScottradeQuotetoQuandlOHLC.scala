package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.QuandlScottrade.Quotes.Scottrade.ConsumeScottradeQuotes

/**
  * Created by nishchaysinha on 11/17/16.
  */
object ScottradeQuotetoQuandlOHLC {

  def run() = {
    ConsumeScottradeQuotes.processDirectory("/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/temp", "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies")
  }

  def main(args: Array[String]) {
    run()
  }

}
