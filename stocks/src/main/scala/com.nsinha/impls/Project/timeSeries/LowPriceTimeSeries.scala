package com.nsinha.impls.Project.TimeSeries

import com.nsinha.impls.Project.TimeSeries.Doubles_ignore.{Size2, TimeSeriesDoubles}

/** Created by nishchaysinha on 10/6/16.
  */
class LowPriceTimeSeries(inFileName : String, key : String = "symbol", axes : List[String] = List("lowprice"),
                         pathToAxisValue : Map[String, List[String]] = Map("lowprice" → List("value")), admitTickers : List[String] = List())
  extends TimeSeries(inFileName, key, axes, pathToAxisValue, None, None, None, admitTickers) {
}

class LowPriceNormalizedTimeSeries(inFileName : String, key : String = "symbol", axes : List[String] = List("lowprice"),
                                   pathToAxisValue : Map[String, List[String]] = Map("lowprice" → List("value")))
  extends TimeSeries(inFileName, key, axes, pathToAxisValue, None, Some(PriceNormalizedTimeSeries.dividePriceByMaximumInRange),
    None, Nil) {
  override def getTransformed = PriceNormalizedTimeSeries.filterByNmonthsContinuousIncreaseSampledAtIntervals(getTransformed1)
  def getTransformed1 = PriceNormalizedTimeSeries.filterPriceByRecentToppings(super.getTransformed)
}

class LowPriceToClosingNormalizedTimeSeries(inFileName : String, key : String = "symbol", axes : List[String] = List("endprice", "lowprice"),
                                            pathToAxisValue : Map[String, List[String]] = Map("endprice" → List("value"), "lowprice" → List("value")))
  extends TimeSeries(inFileName, key, axes, pathToAxisValue, Option(LowPriceTimeSeries.findNormalizedDiff), None, None, Nil) {

}

object LowPriceTimeSeries {
  def findNormalizedDiff(l : List[Double]) : Double = {
    assert(l.length == 2)
    val endPrice = l(0)
    val lowPrice = l(1)
    (endPrice - lowPrice) / endPrice
  }
}

class LowPriceTimeSeries1(inFileName : String, key : String = "symbol", axes : List[String] = List("endprice", "lowprice"),
                          pathToAxisValue : Map[String, List[String]] = Map("endprice" → List("value"), "lowprice" → List("value")))
  extends TimeSeriesDoubles[Size2](inFileName, key, axes, pathToAxisValue, None, None) {

}

