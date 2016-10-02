package com.nsinha.impls.Project.JsonCsvProject

import java.io.File

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.nsinha.data.Project.JsonCsvProject
import org.joda.time.DateTime
import com.nsinha.utils.CsvUtils

import scala.collection.JavaConversions._

/**
  * Created by nishchaysinha on 9/28/16.
  */

case class JsonNodeCsv(name: String, `type`: String = "double")


class JsonCsvProjectImpl(jsonFile: String, modelFile: String, csvFile: String) extends JsonCsvProject {
  val mapper = new ObjectMapper

  val model: Map[String, JsonNodeCsv] = parseModelFile(modelFile)


  def changeAJsonToCsv(): String = {
    val fileJson = new File(jsonFile)
    val tree = mapper.readTree(fileJson)

    //tree must be a object with fields equivalent to row(s)
    assert(tree.getNodeType == JsonNodeType.OBJECT)

    var listOfCols: Set[String] = Set()
    var csvRows = List[List[(String, String)]]()


    if (tree.getNodeType == JsonNodeType.OBJECT) { //we expect fields containing arrays
      val fieldsOfObject: Iterator[java.util.Map.Entry[String, JsonNode]] = tree.fields() //this is keys for rows.
      // every field will lead to an array
      for (rowEntry: java.util.Map.Entry[String, JsonNode] <- fieldsOfObject) {
        if (rowEntry.getValue.getNodeType == JsonNodeType.ARRAY) {
          val (csvRowsCur, listOfColsTemp) = processIfJsonArrayObject(rowEntry, listOfCols)
          listOfCols = listOfCols ++ listOfColsTemp
          csvRows = csvRows ++ csvRowsCur
        } else if ( rowEntry.getValue.getNodeType == JsonNodeType.OBJECT) {
          val (csvRowsCur, listOfColsTemp) = processIfJsonObject(rowEntry, listOfCols)
          listOfCols = listOfCols ++ listOfColsTemp
          csvRows = csvRows ++ csvRowsCur
        }
      }

    }
    CsvUtils.writeCsvHeaderAndRows(csvRows, listOfCols)
  }

  private def processIfJsonObject(rowEntry: java.util.Map.Entry[String, JsonNode], listOfColsGot: Set[String]): (List[List[(String, String)]], Set[String]) = {
    val row = rowEntry.getValue
    var listOfCols = listOfColsGot
    var csvRows = List[List[(String, String)]]()

    //0. curRowList at this time is nil
    var curRowList = List[(String, String)]()
    //1. get all the fields of object
    val fields: Iterator[java.util.Map.Entry[String, JsonNode]] = row.fields()
    //2. update col names if  a new field appear
    listOfCols = listOfCols ++ (row.fieldNames() toList)
    //for each field in obj
    for (f <- fields) {
      val value: JsonNode = f.getValue
      val key = f.getKey
      value.getNodeType match {
        case JsonNodeType.OBJECT =>
          model.get(key) match {
            case None => //ignore
            case Some(jsonNodeCsv) =>
              val x: Option[String] = parseJsonStringUsingModelFromObject(jsonNodeCsv, value)
              x map { y =>
                curRowList = curRowList.::(key -> y)
                y
              }
          }
        case JsonNodeType.ARRAY => ???
        //we dont expect 2nd level array for now. Denormalize this

        case _ if key == "dateTime" =>
          curRowList = curRowList.::(key -> {new DateTime(value.asLong())}.toString("dd-MM-yyyy"))
        case _   =>
          curRowList = curRowList.::(key -> value.asText())
      }

    }
    csvRows = csvRows.::(curRowList)

    (csvRows, listOfCols)

  }

  private def processIfJsonArrayObject(rowEntry: java.util.Map.Entry[String, JsonNode], listOfColsGot: Set[String]): (List[List[(String, String)]], Set[String]) = {
    val row = rowEntry.getValue
    val allRowsInThisArray : Iterator[JsonNode] = row.elements()
    var listOfCols = listOfColsGot
    var csvRows = List[List[(String, String)]]()

    for (row <- allRowsInThisArray) {
      //0. curRowList at this time is nil
      var curRowList = List[(String, String)]()
      //1. get all the fields of object
      val fields: Iterator[java.util.Map.Entry[String, JsonNode]] = row.fields()
      //2. update col names if  a new field appear
      listOfCols = listOfCols ++ (row.fieldNames() toList)
      //for each field in obj
      for (f <- fields) {
        val value: JsonNode = f.getValue
        val key = f.getKey
        value.getNodeType match {
          case JsonNodeType.OBJECT =>
            model.get(key) match {
              case None => //ignore
              case Some(jsonNodeCsv) =>
                val x: Option[String] = parseJsonStringUsingModelFromObject(jsonNodeCsv, value)
                x map { y =>
                  curRowList = curRowList.::(key -> y)
                  y
                }
            }
          case JsonNodeType.ARRAY => ???
          //we dont expect 2nd level array for now. Denormalize this
        case _ if key == "dateTime" =>
          curRowList = curRowList.::(key -> {new DateTime(value.asText())}.toString)
          case _ =>
            curRowList = curRowList.::(key -> value.asText())
        }

      }
      csvRows = csvRows.::(curRowList)
    }

    (csvRows, listOfCols)

  }


  def changeACsvToJson(): String = ???

  def parseModelFile(modelFile: String): Map[String, JsonNodeCsv] = {
    Map[String, JsonNodeCsv]("volume" -> JsonNodeCsv("value"), "initPrice"-> JsonNodeCsv("value"), "endPrice" ->  JsonNodeCsv("value"), "diff" -> JsonNodeCsv("value") )
  }

  private def parseJsonStringUsingModelFromObject(jsonNodeCsv: JsonNodeCsv, node: JsonNode): Option[String] = {
    assert(node.getNodeType == JsonNodeType.OBJECT)
    val interestingNode = node.findPath(jsonNodeCsv.name)
    interestingNode.isMissingNode match {
      case false => Option(interestingNode.asText())
      case true => None
    }
  }
}
