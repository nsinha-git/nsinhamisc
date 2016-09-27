package com.nsinha.data.Csv.generated

import com.nsinha.data.Csv._

/**
  * Created by nishchaysinha on 9/26/16.
  */
class GenCsvOrderRowScottrade (datetime: Long, symbol: String, executionPrice: Price, executedVolume: Volume,  typeOfExecution: Execution) extends  CsvOrderRow
{
  override def getKey(): String = symbol
}

