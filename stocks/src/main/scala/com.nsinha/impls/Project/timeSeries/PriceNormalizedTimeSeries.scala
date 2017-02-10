package com.nsinha.impls.Project.TimeSeries

import scala.collection.mutable

case class TimeSeriesWeighted(key : String, ts : List[Double], weight : Double)

object TimeSeriesWeighted {
  implicit def ordering() : Ordering[TimeSeriesWeighted] = {
    new Ordering[TimeSeriesWeighted] {
      override implicit def compare(x : TimeSeriesWeighted, y : TimeSeriesWeighted) : Int = {
        if (x.weight > y.weight) {
          -1
        }
        else if (x.weight == y.weight) {
          0
        }
        else {
          1
        }
      }
    }
  }
}

object PriceNormalizedTimeSeries {
  def dividePriceByMaximumInRange(x : List[Double], y : (Double, Int)) : Double = {
    val max = x.max
    y._1 / max
  }

  def getTheWinnersPastIntervals(ts : Map[String, List[Double]], lag : Int) : List[(String, List[Double])] = {
    val mp : mutable.Map[String, List[Double]] = mutable.Map()
    val pq : mutable.PriorityQueue[TimeSeriesWeighted] = mutable.PriorityQueue[TimeSeriesWeighted]()(TimeSeriesWeighted.ordering())
    for (kv ← ts) {
      if (kv._2(0) > kv._2(lag)) {
        val diffRate = (kv._2(0) - kv._2(lag)) / kv._2(lag)
        pq.enqueue(TimeSeriesWeighted(kv._1, kv._2, diffRate))
      }
    }
    pq.dequeueAll map (x ⇒ (x.key, x.ts)) toList
  }

  def filterPriceByRecentToppings(ts : Map[String, List[Double]], lag : Int = 14) : Map[String, List[Double]] = {
    val mp : mutable.Map[String, List[Double]] = mutable.Map()
    for (kv ← ts) {
      if (findRecentGoodStats(kv._2, lag)) mp += (kv)
    }
    mp toMap
  }

  def filterByNmonthsContinuousIncreaseSampledAtIntervals(ts : Map[String, List[Double]], lag : Int = 74, intervals : Int = 2) : Map[String, List[Double]] = {
    val mp : mutable.Map[String, List[Double]] = mutable.Map()
    val toBeTested : List[Int] = Range(0, lag + 1, Math.floorDiv(lag, intervals)).toList

    for (kv ← ts) {
      val avgs : List[Double] = toBeTested map { r ⇒ findAverage(kv._2, r) }
      val toBeIncluded = avgs.foldLeft((true, avgs.head)){ (Z, el) ⇒
        if (!Z._1) {
          Z
        }
        else if (el > Z._2) {
          (false, el)
        }
        else {
          (true, el)
        }
      }._1
      if (toBeIncluded) mp += kv

    }
    mp toMap
  }

  def findAverage(l : List[Double], point : Int, window : Int = 7) : Double = {
    val ll = l.slice(point, point + 7)
    (ll.foldLeft(0.0){ (Z, el) ⇒
      Z + el
    }) / (window)
  }

  def findRecentGoodStats(l : List[Double], lag : Int) : Boolean = {
    var sum = 0.0
    var max = 0.0
    for (ll ← l zip Range(0, l.length)) {
      if (ll._2 < lag) {
        if (ll._1 > max) { max = ll._1 }
        sum = sum + ll._1
      }
    }
    if (max >= 0.97 || sum >= 0.92 * lag) {
      true
    }
    else {
      false
    }
  }
}
