package com.nsinha.utils

/**
  * Created by nishchaysinha on 10/3/16.
  */

import java.io.{File, FileInputStream, FileWriter}

import com.nsinha.data.Csv.Price
import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProjectImpl
import com.nsinha.impls.Project.Orders.CsvOrderScottradeProjectImpl
import com.nsinha.impls.Project.Quotes.CsvDailyQuotesScottradeProjectImpl
import com.nsinha.impls.Project.YearlyQuoteAnalysisProject.YearlyQuoteAnalysisProjectImpl
import com.nsinha.utils.{Loggable, StringUtils}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.scalatest.{FunSuite, ShouldMatchers}
import scaldi.Injectable

import scala.io.Source

class UnitTests extends FunSuite with  ShouldMatchers with Injectable with Loggable {

  test("init test for reading csv files") {
    val fileIo = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/file_datestart05302016T16:00:00Zdateend.csv"
    //val fileIo = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/test.csv"
    val source = Source.fromFile(fileIo, "UTF-8")
    for (line <- source.getLines()) {
      val newline = line.replaceAll("\\p{Cntrl}", "?").replaceAll("\\p{Space}", "")

      val x = newline.split(",")
      for (y<-x) {
        val z =StringUtils.extractPrintable(y)
        println(s"$z ${z == "Symbol"}")
      }
    }
  }

}
