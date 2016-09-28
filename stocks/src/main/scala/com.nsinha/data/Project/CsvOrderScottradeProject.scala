package com.nsinha.data.Project

import java.io.File
import com.nsinha.data.Csv.{CsvModel, CsvRow}

/**
  * Created by nishchaysinha on 9/26/16.
  */
trait CsvOrderScottradeProject extends  Project {
  def readModelMap(file: File): CsvModel
  def readCsv(file: File, csvModel: CsvModel): List[CsvRow]
  def dumpPerformance(file: String)
  def dumpPerformanceMostRecentFirst(file: String)
  def dumpPerformanceCurrentHolds(file: String)
}
