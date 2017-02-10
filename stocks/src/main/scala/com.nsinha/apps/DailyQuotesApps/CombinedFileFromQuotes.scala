package com.nsinha.apps.DailyQuotesApps

import com.nsinha.impls.Project.Quotes.Scottrade.ConsumeAllScottradeQuotes

/** Created by nishchaysinha on 11/14/16.
  */
object CombinedFileFromQuotes {
  def run() = {
    val consumeAllQuotes = new ConsumeAllScottradeQuotes("/Users/nishchaysinha/stocksdatadir/currentPerformance")
    consumeAllQuotes()
  }
}
