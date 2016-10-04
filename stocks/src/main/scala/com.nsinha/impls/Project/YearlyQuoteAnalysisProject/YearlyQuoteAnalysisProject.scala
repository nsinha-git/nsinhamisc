package com.nsinha.impls.Project.YearlyQuoteAnalysisProject

import java.io.File

import com.nsinha.data.Project.YearlyQuoteAnalysisProject

import scala.io.Source
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.nsinha.data.Csv.ValueObject
import com.nsinha.data.Csv.generated.GenCsvOrderRowScottrade

import scala.collection.convert.WrapAsScala._
import scala.collection.mutable
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty
/**
  * Created by nishchaysinha on 10/2/16.
  */

object DateAndAxis {
  implicit def ordering(): Ordering[DateAndAxis] = {
    new Ordering[DateAndAxis] {
      def compare(x: DateAndAxis, y: DateAndAxis): Int = {
        if (x.datetime > y.datetime) {
          -1
        } else if (x.datetime == y.datetime) {
          0
        } else {
          1
        }
      }
    }
  }
}

case class DateAndAxis(datetime: Long, axis: ValueObject)

class YearlyQuoteAnalysisProjectImpl(file: String) extends YearlyQuoteAnalysisProject {
  val mapper = new ObjectMapper()

  val dateAxis = "datetimeStart"
  override def createTimeSeries [T <: ValueObject](key: String = "symbol", axisString: String, canBuildT: T): String = {
    implicit val format = DefaultFormats
    val jsonObject = mapper.readTree(new File(file))

    assert(jsonObject.getNodeType == JsonNodeType.ARRAY)
    var mapForKeyToTimeSeriesList: Map[String, List[DateAndAxis]] =  Map()
    for (row: JsonNode <- jsonObject.elements()) {
      val datetime = row.get(dateAxis).asText()
      val symbol = row.get(key).asText()
      val t = row.get(axisString).get("value")
      val axis: ValueObject = ValueObject.setValue(row.get(axisString).get("value").asText(), canBuildT)

      mapForKeyToTimeSeriesList.get(symbol) match {
        case None => mapForKeyToTimeSeriesList = mapForKeyToTimeSeriesList + (symbol -> List(DateAndAxis(datetime.toLong, axis)))
        case Some(list) => val newlist = list.+:(DateAndAxis(datetime.toLong, axis))
          mapForKeyToTimeSeriesList = mapForKeyToTimeSeriesList + (symbol -> newlist)
      }
    }

    //we have map of keys to TS of axis
    val mapOfKeyToTimeSeriesAxis = mapForKeyToTimeSeriesList map { x =>
      val sym = x._1
      val list = transformIntoPriorityQ(x._2) map { y => y.axis}
      sym -> list
    }
    writePretty(mapOfKeyToTimeSeriesAxis)
  }

  private def transformIntoPriorityQ(list :List[DateAndAxis]): List[DateAndAxis] = {
    val priorityQueue = mutable.PriorityQueue[DateAndAxis]()(DateAndAxis.ordering())
    list map ( l => priorityQueue.enqueue(l))
    priorityQueue.toList
  }
}
