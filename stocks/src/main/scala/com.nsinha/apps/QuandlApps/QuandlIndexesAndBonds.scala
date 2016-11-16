package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.QuandlOHLCDump.QuoteForTickerFromQuandl
import com.nsinha.impls.Project.QuandlOHLCDump.QuoteForBondFromQuandl
import com.nsinha.utils.Loggable

import scala.concurrent.{Await, duration}
import scala.concurrent.duration.{Duration, FiniteDuration}


/**
  * Created by nishchaysinha on 11/14/16.
  */


object QuandlIndexesAndBonds extends  Loggable{

 val INDEXES = Map("DJI" -> "https://www.quandl.com/api/v3/datasets/YAHOO/INDEX_DJI.json?api_key=Xmky6espzDoofkY9CFar",
"RUSSELL" -> "https://www.quandl.com/api/v3/datasets/GOOG/AMS_RTWO.json?api_key=Xmky6espzDoofkY9CFar",
"STOXX" -> "https://www.quandl.com/api/v3/datasets/STOXX/PRIND_EURO_CURR.json?api_key=Xmky6espzDoofkY9CFar&start_date=1970-01-01&end_date=1970-01-01",
    "BSE" -> "https://www.quandl.com/api/v3/datasets/BSE/BSE200.json?api_key=Xmky6espzDoofkY9CFar",
   "NIKKEI" -> "https://www.quandl.com/api/v3/datasets/NIKKEI/INDEX.json?api_key=Xmky6espzDoofkY9CFar",
    "HANSENG"-> "https://www.quandl.com/api/v3/datasets/HKEX/00011.json?api_key=Xmky6espzDoofkY9CFar",
    "NASDAQ" -> "https://www.quandl.com/api/v3/datasets/ZEP/NDAQ.json?api_key=Xmky6espzDoofkY9CFar",
"SP500" -> "https://www.quandl.com/api/v3/datasets/YAHOO/INDEX_GSPC.json?api_key=Xmky6espzDoofkY9CFar",
"DAX" -> "https://www.quandl.com/api/v3/datasets/ZEP/DAX.json?api_key=Xmky6espzDoofkY9CFar"
  )

  val BONDS = Map("USTRE" -> "https://www.quandl.com/api/v3/datasets/USTREASURY/YIELD.json?api_key=Xmky6espzDoofkY9CFar")


  def run() = {
    val futIndexes = INDEXES map (kv => QuoteForTickerFromQuandl.createYearlyFilesForMaps(kv._1, "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",kv._2,0))
    val futBonds  = BONDS map (kv => QuoteForBondFromQuandl.createYearlyFilesForMaps(kv._1, "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",kv._2,0))
    //fut map {x => Await.ready(x, FiniteDuration(1, duration.MINUTES))}
  }

  def main(args: Array[String]) {
    run()
  }
}
