package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
trait CsvOrderScottradeProject extends CsvRow {
  def ingestData(file: String)
  def dumpPerformance(file: String)
  def dumpPerformanceMostRecentFirst(file: String)
  def dumpPerformanceCurrentHolds(file: String)
}
