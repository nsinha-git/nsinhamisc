package com.nsinha.applications.currentPerformance.timeSeries


/**
  * Created by nishchaysinha on 10/3/16.
  */
class ClosingPriceTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("endprice"),
                             pathToAxisValue: Map[String, List[String]] = Map("endprice"-> List("value")))
  extends TimeSeries(inFileName, key,axes, pathToAxisValue,None, None) {

}
