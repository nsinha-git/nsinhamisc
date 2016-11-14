package com.nsinha.impls.Project.OrderAnalysis

import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProject
import com.nsinha.impls.Project.Orders.CsvOrderScottradeProjectImpl
import com.nsinha.impls.Project.Quotes.Scottrade.CsvDailyQuotesScottradeProjectImpl
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/**
  * Created by nishchaysinha on 10/20/16.
  */
object OrderAnalysis {
  def apply(quoteFile: String, orderFile: String, modelFileForQuote: String = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt") = {
    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = modelFileForQuote , quotesFilePathInput = quoteFile, classzz = GenCsvQuoteRowScottrade.getClass)
    val orderImpl = new CsvOrderScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", orderFilePathInput = orderFile, quoteImpl)
    val currentHoldsByDate = "/Users/nishchaysinha/stocksdatadir/currentPerformance/currentHoldsByDate.json"
    val currentHoldsBySymbol = "/Users/nishchaysinha/stocksdatadir/currentPerformance/currentHoldsBySymbol.json"

    orderImpl.dumpPerformanceCurrentHolds(currentHoldsByDate)
    orderImpl.dumpPerformanceCurrentHoldsGroupedOnSymbol(currentHoldsBySymbol)

    JsonCsvProject.convertToCsvFile(currentHoldsByDate)
    JsonCsvProject.convertToCsvFile(currentHoldsBySymbol)
  }

}
