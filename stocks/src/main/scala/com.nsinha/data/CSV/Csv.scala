package main.scala.com.nsinha.data.CSV

import java.io.{File, FileOutputStream}
import java.util.Date



abstract class CsvRow(date: Date){
  def maptorows: CsvRow = {
   null
  }
  def getKey: String
}

case class Price(value: Double) extends Ordered[Price] {
  def compare(other: Price): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }
}
object Price {
  def apply(s:String): Price = {
    Price(s.toDouble)
  }
}
case class Volume(value: Double) extends Ordered[Volume] {
   def compare(other: Volume): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }

}
object Volume{
  def apply(s:String): Volume= {
    Volume(s.toDouble)
  }
}

case class Percent(value: Double) extends Ordered[Percent] {
   def compare(other: Percent): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }
}

object Percent{
  def apply(s:String): Percent= {
    Percent(s.toDouble)
  }
}

trait Csv {
  def readModelMap(file: File): CsvModel
  def readCsv(file: File, csvModel: CsvModel, classzz: Class[_]): List[CsvRow]
  def writeTopVolume(i: Int): List[CsvRow]
  def writeTopGainers(i: Int): List[CsvRow]
  def writeTopLoosers(i: Int): List[CsvRow]
  def writeTopFlowers(i: Int): List[CsvRow]
  def extractWatchers(file: File): List[CsvRow]
  def appendToAggregateAnalysisFile[A >: CsvRow](file: File, csvRows: List[A]): Unit
}

case class CsvModel(map:Map[String, String])





