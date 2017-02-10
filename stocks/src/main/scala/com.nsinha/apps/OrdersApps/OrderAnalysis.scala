package com.nsinha.apps.OrdersApps

import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProject
import com.nsinha.impls.Project.Orders.CsvOrderScottradeProjectImpl
import com.nsinha.impls.Project.Quotes.Scottrade.CsvDailyQuotesScottradeProjectImpl
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/** Created by nishchaysinha on 10/20/16.
  */
object OrderAnalysis {
  def apply(quoteFile : String, orderFile : String, byDateFile : String = "/Users/nishchaysinha/stocksdatadir/currentPerformance/currentHoldsByDate.json", bySymbolFile : String = "/Users/nishchaysinha/stocksdatadir/currentPerformance/currentHoldsBySymbol.json",
            modelFileForQuote : String = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt") = {

    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = modelFileForQuote, quotesFilePathInput = quoteFile, classzz = GenCsvQuoteRowScottrade.getClass)
    val orderImpl = new CsvOrderScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", orderFilePathInput = orderFile, quoteImpl)

    orderImpl.dumpPerformanceCurrentHolds(byDateFile)
    orderImpl.dumpPerformanceCurrentHoldsGroupedOnSymbol(bySymbolFile)

    JsonCsvProject.convertToCsvFile(byDateFile)
    JsonCsvProject.convertToCsvFile(bySymbolFile)
  }

  def run() = {
    val quoteFile = "/Users/nishchaysinha/Downloads/Securities_to_Watch2016.12.27.12.50.37.csv"
    val orderFile = "/Users/nishchaysinha/Downloads/CompletedOrders2016.12.27.12.46.57.csv"
    OrderAnalysis(quoteFile, orderFile, "/Users/nishchaysinha/Desktop/byDateHolds.json", "/users/nishchaysinha/Desktop/bySymbolHolds.json")
  }

  def main(args : Array[String]) {
    run()
  }
}
