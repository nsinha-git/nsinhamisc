package com.nsinha.impls.Project.TimeSeries

/**
  * Created by nishchaysinha on 10/12/16.
  */
class VolumeTimeSeries(inFileName: String, key :String = "symbol", axes: List[String] = List("volume"),
                         pathToAxisValue: Map[String, List[String]] = Map("volume" -> List("value")))
  extends TimeSeries(inFileName, key,axes, pathToAxisValue, None, None) {
}
