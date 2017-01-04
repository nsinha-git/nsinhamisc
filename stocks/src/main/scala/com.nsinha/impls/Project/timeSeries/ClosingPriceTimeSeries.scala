package com.nsinha.impls.Project.TimeSeries

import scala.collection.mutable


/**
  * Created by nishchaysinha on 10/3/16.
  */
class ClosingPriceTimeSeries(inFileName: String, key: String = "symbol", axes: List[String] = List("endprice"),
                             pathToAxisValue: Map[String, List[String]] = Map("endprice"-> List("value")), admitTickers: List[String] = List())
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, None, None, admitTickers) {

}


class ClosingPriceNormalizedTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("endprice"),
                                pathToAxisValue: Map[String, List[String]] = Map("endprice"-> List("value")))
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, Some(PriceNormalizedTimeSeries.dividePriceByMaximumInRange),
    None,Nil) {
  override def getTransformed = PriceNormalizedTimeSeries.filterByNmonthsContinuousIncreaseSampledAtIntervals(getTransformed1)
  def getTransformed1 = PriceNormalizedTimeSeries.filterPriceByRecentToppings(super.getTransformed)
}



class OpenPriceTimeSeries(inFileName: String, key: String = "symbol", axes: List[String] = List("startprice"),
                             pathToAxisValue: Map[String, List[String]] = Map("startprice"-> List("value")), admitTickers: List[String])
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, None, None, admitTickers) {

}
