package com.nsinha.impls.Project.QuandlScottrade.Quotes.Scottrade

import java.io.File

import com.nsinha.impls.Project.Quotes.Scottrade.{ConsumeAllScottradeQuotes, CsvDailyQuotesScottradeProjectImpl}
import com.nsinha.utils.{DateTimeUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/**
  * Created by nishchaysinha on 11/17/16.
  */
object ConsumeScottradeQuotes extends  Loggable{

  import ConsumeAllScottradeQuotes._
  def processDirectory(processDir: String, yearlfilesRoot: String) = {
    moveAllFilesToCorrectNameFormat(processDir)
    val processDirFile = new File(processDir)
    val path = processDirFile.getAbsolutePath
    val allFiles = processDirFile.list()

    allFiles map { cur =>
      val fileName = path + "/" + cur
      logger.info(s"Processing $fileName")
      val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", quotesFilePathInput = fileName, classzz = GenCsvQuoteRowScottrade.getClass)
      val (_, dateTime) = DateTimeUtils.parseDate(new File(fileName))
      val year = dateTime.getYear
      quoteImpl.appendDataToRespectiveYearFile(yearlfilesRoot, year)
    }
  }

}
