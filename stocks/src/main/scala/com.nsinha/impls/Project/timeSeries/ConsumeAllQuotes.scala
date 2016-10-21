package com.nsinha.impls.Project.TimeSeries

import java.io.File
import java.nio.file.{FileSystems, Files, Path, StandardCopyOption}

import com.nsinha.impls.Project.Quotes.CsvDailyQuotesScottradeProjectImpl
import com.nsinha.utils.{DateTimeUtils, FileUtils, Loggable, StringUtils}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.joda.time.DateTime

import scala.collection.mutable

/**
  * Created by nishchaysinha on 10/3/16.
  */


class ConsumeAllQuotes (processDir: String, desDirIn: String = "") extends Loggable {
  def consumeTheWholeDirectoryAndMoveToProcessed: Unit = {
    val destDir = if (desDirIn == "") processDir + "/processed" else desDirIn
    moveAllFilesToCorrectNameFormat
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

  def moveAllFilesToCorrectNameFormat: Unit = {
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
