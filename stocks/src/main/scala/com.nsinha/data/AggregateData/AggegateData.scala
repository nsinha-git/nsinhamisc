package main.scala.com.nsinha.data.AggregateData

import java.io.File

import main.scala.com.nsinha.data.CSV.generated.GenCsvRows

trait AggregateData
class AggregateDataVer1Dot0Dot0 extends AggregateData

trait CollationAttributes{
  def map: Map[String, String]
}


class EndCollationAttributes extends CollationAttributes {
  override def map = {
    Map("pricedaymaxloss" -> "",
    "pricedaychange" -> "",
    "pricepercentchangelastdays_"-> "2 4 8 16 32 64 128",
    "pricemaxonedaypercentchangelastdays_"-> "2 4 8 16 32 64 128",
    "volumedaychange" -> "",
    "volumepercentchangelastdays_"-> "2 4 8 16 32 64 128",
    "volumemaxonedaypercentchangelastdays_"-> "2 4 8 16 32 64 128",
    "flowdaychange" -> "",
    "flowpercentchangelastdays_"-> "2 4 8 16 32 64 128",
    "flowmaxonedaypercentchangelastdays_"-> "2 4 8 16 32 64 128"
    )
  }
}

trait CollateTheAggregateData{
  def generateEndValues(dir: File, collationAttributes: CollationAttributes)
  def generateStartEndValues(dir: File, collationAttributes: CollationAttributes)
  def generateStartLowEndValues(dir: File, collationAttributes: CollationAttributes)
  def generateStartHighEndValues(dir: File, collationAttributes: CollationAttributes)
  def generateStartLowHighValues(dir:File, collationAttributes: CollationAttributes)
  def generateValues(dir:File, collationAttributes: CollationAttributes)
}
