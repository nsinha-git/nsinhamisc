package com.nsinha.impls.Project.Quotes.Scottrade

import java.io.File

import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/**
  * Created by nishchaysinha on 10/3/16.
  */


class ConsumeAllScottradeQuotes(processDir: String, desDirIn: String = "") extends Loggable {
  import ConsumeAllScottradeQuotes._
  def apply(): Unit = {
    val destDir = if (desDirIn == "") processDir + "/processed" else desDirIn
    moveAllFilesToCorrectNameFormat(processDir)
    val processDirFile = new File(processDir)
    val path = processDirFile.getAbsolutePath
    val allFiles = processDirFile.list()

    allFiles map { cur =>
      val fileName = path + "/" + cur
      if (new File(fileName).isDirectory) {
      } else {
        logger.info(s"Processing $fileName")
        val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", quotesFilePathInput= fileName, classzz = GenCsvQuoteRowScottrade.getClass)
        val (_, dateTime) = DateTimeUtils.parseDate(new File(fileName))
        val year = dateTime.getYear
        quoteImpl.appendDataToYearFile(path + "/output/yearly/" + year + "/metaCombinedData.json", path + "/output/yearly/" + year + "/combinedData.json")
        FileUtils.moveFileToDestDir(fileName, destDir)
      }
    }
  }


}


object ConsumeAllScottradeQuotes {
  def moveAllFilesToCorrectNameFormat(processDir: String): Unit = {
    val processDirFile = new File(processDir)
    val path = processDirFile.getAbsolutePath
    val allFiles = processDirFile.list()
    allFiles map { cur =>
      val curFile = new File(path + "/" + cur)
      if (curFile.isDirectory) {
      } else {
        val newfilename = if (cur.contains("datestart")) cur else FileUtils.renameAStringToHaveDateFormat(cur)
        if (newfilename != cur) {
          FileUtils.moveFile(path + "/" + cur, path + "/" + newfilename)
        }
      }
    }
  }
}
