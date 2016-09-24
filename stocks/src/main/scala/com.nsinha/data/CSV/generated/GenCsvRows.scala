package main.scala.com.nsinha.data.CSV.generated


import java.util.Date

import main.scala.com.nsinha.data.CSV.{CsvRow, Percent, Price, Volume}

case class GenCsvRows (date: Date, symbol: String, endprice: Price, startprice: Price, highprice: Price, lowprice: Price, volume: Volume, companyname: String, percentagechange: Percent) extends CsvRow(date)
{
  override def getKey(): String = symbol
}

object GenCsvRows
{
  def sort[A <: CsvRow,B <: CsvRow] (by: String, asc: Boolean): Function2[GenCsvRows, GenCsvRows, Boolean] = {
    val fn : Function2[GenCsvRows,GenCsvRows,Boolean]=
      (by, asc) match {
        case ("volume", true) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if (x.volume >= y.volume) true else false
          }
        }
        case ("volume", false) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if (x.volume <= y.volume) true else false
          }
        }
        case ("percent", true) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if (x.percentagechange >= y.percentagechange) true else false
          }
        }
        case ("percent", false) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if (x.percentagechange <= y.percentagechange) true else false
          }
        }
        case ("flow", true) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if ((x.percentagechange.value* x.volume.value) >= (y.percentagechange.value * y.volume.value)) true else false
          }
        }
        case ("flow", false) => new Function2[GenCsvRows, GenCsvRows, Boolean] {
          override def apply(x: GenCsvRows, y: GenCsvRows): Boolean = {
            if ((x.percentagechange.value* x.volume.value) <= (y.percentagechange.value * y.volume.value)) true else false
          }
        }
      }
      fn

    }
}

