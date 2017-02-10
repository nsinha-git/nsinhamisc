package com.nsinha.data.Csv.generated

import com.nsinha.data.Csv.{CsvOrderRow, Execution, Price, Volume}

/** Created by nishchaysinha on 11/15/16.
  */

case class GenCsvBondRow(datetime : Long, symbol : String, price : Price) extends CsvOrderRow {
  override def getKey() : String = symbol
}

object GenCsvBondRow {
  implicit def ordering() : Ordering[GenCsvBondRow] = {
    new Ordering[GenCsvBondRow] {
      def compare(x : GenCsvBondRow, y : GenCsvBondRow) : Int = {
        if (x.datetime > y.datetime) {
          -1
        }
        else if (x.datetime == y.datetime) {
          0
        }
        else {
          1
        }
      }
    }
  }
}

