package com.nsinha.impls.Project.timeSeries

/**
  * Created by nishchaysinha on 10/6/16.
  */



class LowPriceTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("endprice", "lowprice"),
                             pathToAxisValue: Map[String, List[String]] = Map("endprice"-> List("value"), "lowprice" -> List("value")))
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, None) {

}


class LowPriceNormalizedTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("endprice",lowprice"),
                                   pathToAxisValue: Map[String, List[String]] = Map("endprice"-> List("value"), "lowprice" -> List("value")))
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, Some(PriceNormalizedTimeSeries.dividePriceByMaximumInRange),
    None) {
  override def getTransformed = PriceNormalizedTimeSeries.filterByNmonthsContinuousIncreaseSampledAtIntervals(getTransformed1)
  def getTransformed1 = PriceNormalizedTimeSeries.filterPriceByRecentToppings(super.getTransformed)
}

