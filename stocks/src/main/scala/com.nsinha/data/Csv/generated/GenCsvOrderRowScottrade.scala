package com.nsinha.data.Csv.generated

import com.nsinha.data.Csv._

/** Created by nishchaysinha on 9/26/16.
  */
case class GenCsvOrderRowScottrade(datetime : Long, symbol : String, executionPrice : Price, executedVolume : Volume, typeOfExecution : Execution) extends CsvOrderRow {
  override def getKey() : String = symbol
}

object GenCsvOrderRowScottrade {
  implicit def ordering() : Ordering[GenCsvOrderRowScottrade] = {
    new Ordering[GenCsvOrderRowScottrade] {
      def compare(x : GenCsvOrderRowScottrade, y : GenCsvOrderRowScottrade) : Int = {
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

