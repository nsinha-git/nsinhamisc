package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.JsonCsvProject.ConcatenateJsonFiles

/**
  * Created by nishchaysinha on 11/14/16.
  */
object SingleYearlyFile {

  def run() = {
    for (year <- Range(2011,2017)) {
      ConcatenateJsonFiles.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}/normalized", outputfile = "combinedData.json")
    }
  }

  def main(args: Array[String]) {
    run()
  }

}
