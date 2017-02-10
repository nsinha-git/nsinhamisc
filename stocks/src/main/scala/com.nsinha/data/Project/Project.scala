package com.nsinha.data.Project

import com.nsinha.data.Csv._
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade

/** Created by nishchaysinha on 9/27/16.
  */
trait Project {
  def encode(line : String) : String = {
    val splitsAtColon = line.split("\"")
    var ll : String = ""
    for (i ← Range(0, splitsAtColon.length)) {
      val x = i % 2 match {
        case 1 ⇒ splitsAtColon(i).replaceAll(",", "")
        case _ ⇒ splitsAtColon(i)
      }
      ll = ll + x
    }
    ll.replaceAll("\\+", "").replaceAll("-", "").replaceAll("%", "")
  }

  def extractRowCols(line : String, cols : Map[String, Int]) : Map[String, String] = {
    val allCols = encode(line).split(",")
    var res : Map[String, String] = Map()
    for (x ← cols.keys) {
      val y = allCols(cols(x))
      res = res + (x → y)
    }
    res
  }

  def createColsList(line : String, csvModel : CsvModel, prefix : String) : Map[String, Int] = {
    val preModelCols = line.split(",").map(_.trim)
    val cols : Map[String, Int] = mapModel(csvModel, preModelCols, prefix)
    cols
  }

  def mapModel(csvModel : CsvModel, preModelCols : Array[String], prefix : String) : Map[String, Int] = {
    var map : Map[String, Int] = Map()
    var i = 0
    val modelMap = csvModel.map
    for (preModelCol ← preModelCols) {
      val mappedCol = modelMap.get(preModelCol.toString)
      mappedCol match {
        case None ⇒
        case Some(x) ⇒
          map = map + (s"$x" → i)
      }
      i = i + 1
    }
    map
  }

}
