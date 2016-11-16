package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.JsonCsvProject.ConcatenateJsonFiles
import com.nsinha.impls.Project.QuandlOHLCDump.NormalizedOHLC.NormalizeTickers

/**
  * Created by nishchaysinha on 11/15/16.
  */
object NormalizedQuotesLatestAsNormal {
  def run() = {
    for (year <- Range(2000,2017)) {
      NormalizeTickers.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}")
    }
  }

  def main(args: Array[String]) {
    run()
  }


}
