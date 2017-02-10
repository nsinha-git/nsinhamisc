package com.nsinha.data.Project

import java.io.File

import com.nsinha.data.Csv.{CsvModel, CsvQuoteRow, Price}
import org.joda.time.DateTime

trait CsvDailyQuotesScottradeProject extends Project {
  def readModelMap(file : File) : CsvModel
  def readSingleDayGroupedQuotesCsv(file : File, csvModel : CsvModel, classzz : Class[_]) : List[CsvQuoteRow]
  def getQuoteForToday(symbol : String, `type` : String) : Price
  def writeTopVolumesForToday(i : Int) : List[CsvQuoteRow]
  def writeTopGainersForToday(i : Int) : List[CsvQuoteRow]
  def writeTopLoosersForToday(i : Int) : List[CsvQuoteRow]
  def writeTopFlowersForToday(i : Int) : List[CsvQuoteRow]
  def extractWatchersForToday(file : File) : List[CsvQuoteRow]
  def appendToAggregateAnalysisFile[A >: CsvQuoteRow](file : String, csvRows : List[A]) : Unit
  def appendDataToYearFile(metaDataYearFile : String, yearFile : String) : Unit
  def appendDataToRespectiveYearFile(yearFileDirRoot : String, year : Int) : Unit
}

