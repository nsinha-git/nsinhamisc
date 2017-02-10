package com.nsinha.impls.Project.TimeSeries

import java.io.File

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.nsinha.utils.Loggable

import scala.collection.convert.WrapAsScala._
import scala.collection.mutable

/** Created by nishchaysinha on 10/3/16.
  */
object TimeSeries {
  def induceTransform(ts : Map[String, List[Double]], fn : (List[Double], (Double, Int)) ⇒ Double) : Map[String, List[Double]] = {
    ts map { kv ⇒
      val key = kv._1
      val value = kv._2
      val zippedValue = value zip Range(0, value.length)
      val trans = zippedValue map { el ⇒ fn(value, el) }
      key → trans
    }
  }
}

class TimeSeries(inputJsonFileName : String, key : String = "symbol", axes : List[String], pathToAxisValue : Map[String, List[String]] = Map(),
                 fnOpt : Option[List[Double] ⇒ Double] = None, transformFnOpt : Option[(List[Double], (Double, Int)) ⇒ Double] = None,
                 filterFn : Option[(Map[String, List[Double]]) ⇒ Map[String, List[Double]]] = None, tickerToAdmit : List[String]) extends Loggable {
  val inputFile = new File(inputJsonFileName)
  val mapper = new ObjectMapper()
  val timeSeries : Map[String, List[Double]] = processTimeSeries()
  val transFormedTimeSeries : Map[String, List[Double]] = transform()

  def get : Map[String, List[Double]] = timeSeries
  def getTransformed : Map[String, List[Double]] = transFormedTimeSeries

  def processTimeSeries() : Map[String, List[Double]] = {
    val rootNode = mapper.readTree(inputFile)
    val map1 : scala.collection.mutable.Map[String, List[Double]] = mutable.Map[String, List[Double]]()

    assert(rootNode.getNodeType == JsonNodeType.ARRAY)

    for (elem : JsonNode ← rootNode.elements()) {
      val symbol = elem.path(key).asText()
      if ((tickerToAdmit.length > 0 && tickerToAdmit.contains(symbol)) || (tickerToAdmit.length == 0)) {
        val axesValues : List[Double] = axes map { axis ⇒
          val pathToAxisOpt : Option[List[String]] = pathToAxisValue.get(axis)
          pathToAxisOpt map { pathToAxis ⇒
            val value = pathToAxis.foldLeft(elem.path(axis))((Z, e) ⇒
              Z.path(e)).asDouble()
            value
          } getOrElse (elem.asDouble())
        }
        val scalarValue : Double = if (axesValues.length > 1) {
          val x = fnOpt map { fn ⇒ fn(axesValues) }
          x.get
        }
        else axesValues.head

        map1.get(symbol) match {
          case None ⇒ map1 += (symbol → List(scalarValue))
          case Some(x) ⇒
            map1.remove(symbol)
            val newList = x.+:(scalarValue)
            map1 += (symbol → newList)
        }
      }
    }

    //map1 is fully built now

    val map2 = lengthNormalizeTheTimeSeries(map1.toMap)
    map2
  }

  def findTheMaxLengthOfTimeSeries(mp : Map[String, List[Double]]) : Int = {
    mp.foldLeft(0){ (Z, el) ⇒
      if (Z < el._2.length) el._2.length else Z
    }
  }

  def lengthNormalizeTheTimeSeries(map1 : Map[String, List[Double]]) : Map[String, List[Double]] = {
    val maxLen = findTheMaxLengthOfTimeSeries(map1)
    map1 map { elem ⇒
      elem._1 → lengthNormalizeTheTimeSeries(elem._2, maxLen)
    }
  }

  def lengthNormalizeTheTimeSeries(l : List[Double], maxLen : Int) : List[Double] = {
    if (l.length < maxLen) {
      val zeros = Range(0, (maxLen - l.length)) map { x ⇒ 0.0 }
      val ll = l.++(zeros)
      assert(ll.length == maxLen)
      ll
    }
    else l
  }

  def transform() : Map[String, List[Double]] = {
    transformFnOpt match {
      case None    ⇒ timeSeries
      case Some(f) ⇒ TimeSeries.induceTransform(timeSeries, f)
    }
  }
}
