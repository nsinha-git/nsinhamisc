package com.nsinha.apps.DailyQuotesApps

import com.nsinha.impls.Project.Quotes.Scottrade.CsvDailyQuotesScottradeProjectImpl
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/** Created by nishchaysinha on 11/14/16.
  */
object DailyQuotes {

  def run() = {
    val currentQuoteFile = "/Users/nishchaysinha/stocksdatadir/currentPerformance/processed/Securities_to_Watchdatestart2016-10-06T20:22:28Zdateend.csv"
    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", quotesFilePathInput = currentQuoteFile, classzz = GenCsvQuoteRowScottrade.getClass)
    quoteImpl.appendDataToYearFile("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/metafile2016.txt", "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val topFlowers = quoteImpl.writeTopFlowersForToday(10)
  }

  def main(args : Array[String]) {
    run()
  }

}
