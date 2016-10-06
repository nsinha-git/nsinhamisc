package com.nsinha.impls.Project.timeSeries

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

object ConsumeAllQuotes {

  def parensInString(s: String): Int = {
    val x = StringUtils.countThisCharInString(s,'(')
    val y = StringUtils.countThisCharInString(s,')')
    assert (x == y)
    x
  }

  def renameAStringToHaveDateFormat(s: String, format: Tuple2[String,List[String]] = Tuple2("([a-zA-Z_]*)(\\d*).(\\d*).(\\d*).(\\d*).(\\d*).(\\d*).([a-zA-Z]*)",List("prefix",
    "year", "month", "date", "hours", "mins", "secs", "suffix"))): String = {
    val re = format._1.r
    val m = re.findAllMatchIn(s)
    assert(!m.isEmpty)
    val match1 = m.next()
    val parens = parensInString(format._1)
    assert(match1.groupCount == parens)
    assert(format._2.size == parens)
    val map = mutable.Map[String, String]()
    for (i <- Range(1, parens+1)) {
      map(format._2(i-1)) = match1.group(i)
    }
    val newFileName = map("prefix")+"datestart" + map("year") + "-" + map("month") + "-" + map("date") + "T" +  map("hours") + ":" + map("mins") + ":" +  map("secs") + "Zdateend." + map("suffix")
    newFileName
  }

  def main(args: Array[String]) {
  }
}


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
        val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", csvFilePath = fileName, classzz = GenCsvQuoteRowScottrade.getClass)
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
        val newfilename = if (cur.contains("datestart")) cur else ConsumeAllQuotes.renameAStringToHaveDateFormat(cur)
        if (newfilename != cur) {
          FileUtils.moveFile(path + "/" + cur, path + "/" + newfilename)
        }
      }
    }
  }
}
