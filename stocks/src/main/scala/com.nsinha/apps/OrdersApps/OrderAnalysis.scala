package com.nsinha.apps.OrdersApps

import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProject
import com.nsinha.impls.Project.Orders.CsvOrderScottradeProjectImpl
import com.nsinha.impls.Project.Quotes.Scottrade.CsvDailyQuotesScottradeProjectImpl
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/** Created by nsinha on 10/20/16.
  */
object OrderAnalysis {
  def apply(quoteFile : String, orderFile : String, byDateFile : String = "/Users/nsinha/stocksdatadir/currentPerformance/currentHoldsByDate.json", bySymbolFile : String = "/Users/nsinha/stocksdatadir/currentPerformance/currentHoldsBySymbol.json",
            modelFileForQuote : String = "/Users/nsinha/mygithubs/nsinhamisc/stocks/src/test/resources/modelforcsv.txt") = {

    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = modelFileForQuote, quotesFilePathInput = quoteFile, classzz = GenCsvQuoteRowScottrade.getClass)
    val orderImpl = new CsvOrderScottradeProjectImpl(modelFilePath = "/Users/nsinha/mygithubs/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", orderFilePathInput = orderFile, quoteImpl)

    orderImpl.dumpPerformanceCurrentHolds(byDateFile)
    orderImpl.dumpPerformanceCurrentHoldsGroupedOnSymbol(bySymbolFile)

    JsonCsvProject.convertToCsvFile(byDateFile)
    JsonCsvProject.convertToCsvFile(bySymbolFile)
  }

  def run() = {
    val quoteFile = "/Users/nsinha/Downloads/Securities_to_Watch2017.02.28.15.09.32.csv"
    val orderFile = "/Users/nsinha/Downloads/CompletedOrders2017.02.28.14.39.49.csv"
    OrderAnalysis(quoteFile, orderFile, "/Users/nsinha/Desktop/byDateHolds.json", "/users/nsinha/Desktop/bySymbolHolds.json")
  }

  def main(args : Array[String]) {
    run()
  }
}
