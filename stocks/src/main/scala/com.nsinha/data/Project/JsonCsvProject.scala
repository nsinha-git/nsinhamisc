package com.nsinha.data.Project

/** Created by nishchaysinha on 9/28/16.
  */
trait JsonCsvProject extends Project {
  def changeAJsonToCsv() : String
  def changeACsvToJson() : String
  def changeAJsonToTsCsv() : String
}
