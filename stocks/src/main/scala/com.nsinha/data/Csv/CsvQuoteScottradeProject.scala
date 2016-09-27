package main.scala.com.nsinha.data.Csv

import java.io.{File, FileOutputStream}
import java.util.Date
import com.nsinha.data.Csv.{CsvModel, CsvQuoteRow, CsvRow}
import org.joda.time.DateTime

trait CsvQuoteScottradeProject {
  def readModelMap(file: File): CsvModel
  def readCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[CsvQuoteRow]
  def getDates(file: File): List[DateTime]
  def writeTopVolume(i: Int): List[CsvQuoteRow]
  def writeTopGainers(i: Int): List[CsvQuoteRow]
  def writeTopLoosers(i: Int): List[CsvQuoteRow]
  def writeTopFlowers(i: Int): List[CsvQuoteRow]
  def extractWatchers(file: File): List[CsvQuoteRow]
  def appendToAggregateAnalysisFile[A >: CsvQuoteRow](file: File, csvRows: List[A]): Unit
  def appendDataToYearFile(metaDataYearFile: String,  yearFile: String): Unit
}






