package com.nsinha.impls.Project.timeSeries

import scala.collection.mutable

/**
  * Created by nishchaysinha on 10/6/16.
  */
object PriceNormalizedTimeSeries {
  def dividePriceByMaximumInRange(x: List[Double], y: (Double, Int)) : Double = {
    val max = x.max
    y._1/max
  }

  def filterPriceByRecentToppings(ts: Map[String, List[Double]], lag: Int = 14): Map[String, List[Double]]  = {
    val mp: mutable.Map[String, List[Double]] = mutable.Map()

    for (kv <- ts) {
      print(kv._1)
      if (findRecentGoodStats(kv._2, lag)) mp +=(kv)
    }
    mp toMap
  }

  def filterByNmonthsContinuousIncreaseSampledAtIntervals(ts: Map[String, List[Double]], lag: Int = 74, intervals: Int = 2): Map[String, List[Double]]  = {
    val mp: mutable.Map[String, List[Double]] = mutable.Map()
    val toBeTested: List[Int] = Range(0, lag + 1, Math.floorDiv(lag,intervals)).toList

    for (kv <- ts) {
      val avgs: List[Double] = toBeTested map {r => findAverage(kv._2,r)}
      val toBeIncluded = avgs.foldLeft((true,avgs.head)){(Z,el) =>
       if (!Z._1) {
         Z
       } else if (el > Z._2) {
         (false, el)
       } else {
         (true, el)
       }
      }._1
      if (toBeIncluded) mp += kv

    }
    mp toMap
  }

  def findAverage(l: List[Double], point: Int, window: Int =7): Double = {
    val ll = l.slice(point, point + 7 )
    (ll.foldLeft(0.0){(Z,el) =>
      Z + el
    }) / (window)
  }


  def findRecentGoodStats(l: List[Double], lag: Int): Boolean = {
    var sum = 0.0
    var max = 0.0
    for ( ll <-l zip Range(0,l.length)){
      if (ll._2 < lag) {
        if (ll._1 > max) {max = ll._1}
        sum = sum  + ll._1
      }
    }
    println( s" $max ${sum/lag}" )
    if (max >= 0.97 ||  sum >= 0.92*lag) {
      true
    } else {
      false
    }
  }
}
