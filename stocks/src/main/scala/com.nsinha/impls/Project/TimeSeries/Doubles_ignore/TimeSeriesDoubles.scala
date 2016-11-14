package com.nsinha.impls.Project.TimeSeries.Doubles_ignore

import java.io.File

import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nsinha.utils.Loggable

import scala.collection.convert.WrapAsScala._
import scala.collection.mutable

object TimeSeriesDoubles {
  def induceTransform(ts: Map[String, List[Double]], fn: (List[Double], (Double, Int)) => Double): Map[String, List[Double]] = {
    ts map { kv =>
      val key = kv._1
      val value = kv._2
      val zippedValue = value zip Range(0, value.length)
      val trans = zippedValue map {el => fn(value, el)}
      key -> trans
    }
  }
}

class TimeSeriesDoubles[A<:Size](inputJsonFileName: String, key: String = "symbol", axes: List[String] , pathToAxisValue:Map[String,List[String]] = Map(),
                 fnOpt: Option[List[Doubles[A]] => Doubles[A]] = None, transformFnOpt: Option[(List[Doubles[A]], (Doubles[A], Int)) => Doubles[A]] = None,
                 filterFn: Option[(Map[String, List[Doubles[A]]]) => Map[String, List[Doubles[A]]]] = None) extends  Loggable {
  val inputFile = new File(inputJsonFileName)
  val mapper = new ObjectMapper()
  val timeSeries: Map[String, List[Doubles[A]]] = processTimeSeries()
  val transFormedTimeSeries : Map[String, List[Doubles[A]]] = transform()

  def get: Map[String, List[Doubles[A]]] = timeSeries
  def getTransformed: Map[String, List[Doubles[A]]] = transFormedTimeSeries

  def processTimeSeries(): Map[String, List[Doubles[A]]]  = {
    val rootNode = mapper.readTree(inputFile)
    val map1: scala.collection.mutable.Map[String, List[Doubles[A]]] = mutable.Map[String,List[Doubles[A]]]()

    assert(rootNode.getNodeType == JsonNodeType.ARRAY)

    for (elem: JsonNode <- rootNode.elements()) {
      val symbol = elem.path(key).asText()
      val axesValues: List[Double] = axes map {axis =>
        val pathToAxisOpt: Option[List[String]] = pathToAxisValue.get(axis)
        pathToAxisOpt map { pathToAxis =>
          val value = pathToAxis.foldLeft(elem.path(axis))((Z, e) =>
            Z.path(e)
          ).asDouble()
          value
        } getOrElse(elem.asDouble())
      }
      val doublesValue : Doubles[A] = null//new Doubles[A](axesValues)


      map1.get(symbol) match {
        case None => map1 += (symbol-> List(doublesValue))
        case Some(x) => map1.remove(symbol)
          val newList = x.+:(doublesValue)
          map1 += (symbol -> newList)
      }
    }

    //map1 is fully built now

    val map2 = lengthNormalizeTheTimeSeries(map1.toMap)
    map2
  }

  def findTheMaxLengthOfTimeSeries(mp: Map[String, List[Doubles[_]]]): Int = {
    mp.foldLeft(0){ (Z, el) =>
      if (Z < el._2.length) el._2.length else Z
    }
  }

  def lengthNormalizeTheTimeSeries(map1: Map[String, List[Doubles[A]]]): Map[String, List[Doubles[A]]] = {
    val maxLen = findTheMaxLengthOfTimeSeries(map1)
    map1 map {elem =>
      elem._1 -> lengthNormalizeTheTimeSeries(elem._2, maxLen)
    }
  }

  def lengthNormalizeTheTimeSeries(l: List[Doubles[A]], maxLen: Int): List[Doubles[A]] = {
    if (l.length < maxLen) {
      val zeros: List[Doubles[A]] = Range(0, (maxLen - l.length)).toList map {x => l.head.zero()}
      val ll = l.++(zeros)
      assert(ll.length == maxLen)
      ll
    } else l
  }

  def transform(): Map[String, List[Doubles[A]]] = {
    ???
  }
}

