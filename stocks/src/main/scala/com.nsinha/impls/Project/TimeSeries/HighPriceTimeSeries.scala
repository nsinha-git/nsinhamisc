package com.nsinha.impls.Project.TimeSeries

/**
  * Created by nishchaysinha on 10/12/16.
  */

class HighPriceTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("highprice"),
                         pathToAxisValue: Map[String, List[String]] = Map("highprice" -> List("value")), admitTickers: List[String] = List())
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, None, None,admitTickers) {
}


