package com.nsinha.impls.Project.JsonCsvProject

import java.io.{File, FileWriter}

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.nsinha.data.Project.JsonCsvProject
import org.joda.time.DateTime
import scala.collection.JavaConversions._

/** Created by nishchaysinha on 9/28/16.
  */

case class JsonNodeCsv(name : String, `type` : String = "double")

class JsonCsvProjectImpl(jsonFile : String, modelFile : String, csvFile : String) extends JsonCsvProject {
  val mapper = new ObjectMapper

  val model : Map[String, JsonNodeCsv] = parseModelFile(modelFile)

  override def changeAJsonToTsCsv() : String = {
    val fileJson = new File(jsonFile)
    val tree = mapper.readTree(fileJson)

    //tree must be a object with fields equivalent to row(s)
    assert(tree.getNodeType == JsonNodeType.OBJECT || tree.getNodeType == JsonNodeType.ARRAY)

    //var listOfCols: Set[String] = Set()
    var csvRows = List[List[(String, String)]]()
    var keyToList : List[(String, List[Double])] = List()

    if (tree.getNodeType == JsonNodeType.OBJECT) {
      val fieldsOfObject : Iterator[java.util.Map.Entry[String, JsonNode]] = tree.fields() //this is keys for rows.

      keyToList = fieldsOfObject map { rowEntry : java.util.Map.Entry[String, JsonNode] ⇒
        assert(rowEntry.getValue.getNodeType == JsonNodeType.ARRAY)
        val key = rowEntry.getKey
        val arrayOfDouble = rowEntry.getValue
        val allRowsInThisArray : Iterator[JsonNode] = arrayOfDouble.elements()
        val listOfDoubles = allRowsInThisArray.foldLeft(List[Double]()){ (Z, el) ⇒ Z.:+(el.asDouble()) }
        key → listOfDoubles
      } toList
    }
    else if (tree.getNodeType == JsonNodeType.ARRAY) {
      val objsOfArray : List[JsonNode] = tree.elements() toList //this is keys for rows.

      keyToList = objsOfArray.map { jsonNode ⇒
        assert(jsonNode.getNodeType == JsonNodeType.OBJECT)
        val key = jsonNode.fieldNames().next()
        val arrayOfDouble = jsonNode.path(key)
        val allRowsInThisArray : Iterator[JsonNode] = arrayOfDouble.elements()
        val listOfDoubles = allRowsInThisArray.foldLeft(List[Double]()){ (Z, el) ⇒ Z.:+(el.asDouble()) }
        key → listOfDoubles
      } toList
    }
    var listOfCols : List[String] = List("symbol")
    listOfCols = listOfCols ++ Range(0, keyToList.head._2.length) map (_.toString)

    csvRows = keyToList.foldLeft(List[List[(String, String)]]()) { (Z, kv) ⇒
      val sym = kv._1
      val listOfDoubles = kv._2
      var l = List[(String, String)](("symbol", sym))
      for (elem ← listOfDoubles zip Range(0, listOfDoubles.length)) {
        l = l.:+(elem._2.toString, elem._1.toString)
      }
      Z :+ l
    }
    CsvUtils.writeCsvHeaderAndRows(csvRows, listOfCols)
  }

  override def changeAJsonToCsv() : String = {
    val fileJson = new File(jsonFile)
    val tree = mapper.readTree(fileJson)

    //tree must be a object with fields equivalent to row(s)
    assert(tree.getNodeType == JsonNodeType.OBJECT)

    var listOfCols : Set[String] = Set()
    var csvRows = List[List[(String, String)]]()

    if (tree.getNodeType == JsonNodeType.OBJECT) { //we expect fields containing arrays
      val fieldsOfObject : Iterator[java.util.Map.Entry[String, JsonNode]] = tree.fields() //this is keys for rows.
      // every field will lead to an array
      for (rowEntry : java.util.Map.Entry[String, JsonNode] ← fieldsOfObject) {
        if (rowEntry.getValue.getNodeType == JsonNodeType.ARRAY) {
          val (csvRowsCur, listOfColsTemp) = processIfJsonArrayObject(rowEntry, listOfCols)
          listOfCols = listOfCols ++ listOfColsTemp
          csvRows = csvRows ++ csvRowsCur
        }
        else if (rowEntry.getValue.getNodeType == JsonNodeType.OBJECT) {
          val (csvRowsCur, listOfColsTemp) = processIfJsonObject(rowEntry, listOfCols)
          listOfCols = listOfCols ++ listOfColsTemp
          csvRows = csvRows ++ csvRowsCur
        }
      }

    }
    CsvUtils.writeCsvHeaderAndRows(csvRows, listOfCols)
  }

  private def processIfJsonObject(rowEntry : java.util.Map.Entry[String, JsonNode], listOfColsGot : Set[String]) : (List[List[(String, String)]], Set[String]) = {
    val row = rowEntry.getValue
    var listOfCols = listOfColsGot
    var csvRows = List[List[(String, String)]]()

    //0. curRowList at this time is nil
    var curRowList = List[(String, String)]()
    //1. get all the fields of object
    val fields : Iterator[java.util.Map.Entry[String, JsonNode]] = row.fields()
    //2. update col names if  a new field appear
    listOfCols = listOfCols ++ (row.fieldNames() toList)
    //for each field in obj
    for (f ← fields) {
      val value : JsonNode = f.getValue
      val key = f.getKey
      value.getNodeType match {
        case JsonNodeType.OBJECT ⇒
          model.get(key) match {
            case None ⇒ //ignore
            case Some(jsonNodeCsv) ⇒
              val x : Option[String] = parseJsonStringUsingModelFromObject(jsonNodeCsv, value)
              x map { y ⇒
                curRowList = curRowList.::(key → y)
                y
              }
          }
        case JsonNodeType.ARRAY ⇒ ???
        //we dont expect 2nd level array for now. Denormalize this

        case _ if key == "dateTime" ⇒
          curRowList = curRowList.::(key → { new DateTime(value.asLong()) }.toString("MM/dd/yyyy"))
        case _ ⇒
          curRowList = curRowList.::(key → value.asText())
      }

    }
    csvRows = csvRows.::(curRowList)

    (csvRows, listOfCols)

  }

  private def processIfJsonArrayObject(rowEntry : java.util.Map.Entry[String, JsonNode], listOfColsGot : Set[String]) : (List[List[(String, String)]], Set[String]) = {
    val row = rowEntry.getValue
    val allRowsInThisArray : Iterator[JsonNode] = row.elements()
    var listOfCols = listOfColsGot
    var csvRows = List[List[(String, String)]]()

    for (row ← allRowsInThisArray) {
      //0. curRowList at this time is nil
      var curRowList = List[(String, String)]()
      //1. get all the fields of object
      val fields : Iterator[java.util.Map.Entry[String, JsonNode]] = row.fields()
      //2. update col names if  a new field appear
      listOfCols = listOfCols ++ (row.fieldNames() toList)
      //for each field in obj
      for (f ← fields) {
        val value : JsonNode = f.getValue
        val key = f.getKey
        value.getNodeType match {
          case JsonNodeType.OBJECT ⇒
            model.get(key) match {
              case None ⇒ //ignore
              case Some(jsonNodeCsv) ⇒
                val x : Option[String] = parseJsonStringUsingModelFromObject(jsonNodeCsv, value)
                x map { y ⇒
                  curRowList = curRowList.::(key → y)
                  y
                }
            }
          case JsonNodeType.ARRAY ⇒ ???
          //we dont expect 2nd level array for now. Denormalize this
          case _ if key == "dateTime" ⇒
            curRowList = curRowList.::(key → { new DateTime(value.asText().toLong) }.toString("MM/dd/yyyy"))
          case _ ⇒
            curRowList = curRowList.::(key → value.asText())
        }

      }
      csvRows = csvRows.::(curRowList)
    }

    (csvRows, listOfCols)

  }

  def changeACsvToJson() : String = ???

  def parseModelFile(modelFile : String) : Map[String, JsonNodeCsv] = {
    Map[String, JsonNodeCsv]("volume" → JsonNodeCsv("value"), "initPrice" → JsonNodeCsv("value"), "endPrice" → JsonNodeCsv("value"), "diff" → JsonNodeCsv("value"))
  }

  private def parseJsonStringUsingModelFromObject(jsonNodeCsv : JsonNodeCsv, node : JsonNode) : Option[String] = {
    assert(node.getNodeType == JsonNodeType.OBJECT)
    val interestingNode = node.findPath(jsonNodeCsv.name)
    interestingNode.isMissingNode match {
      case false ⇒ Option(interestingNode.asText())
      case true  ⇒ None
    }
  }
}

object JsonCsvProject {
  def convertToCsvFile(jsonFileName : String) {
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "", jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToCsv()
    val fw = new FileWriter(jsonFileName+".csv")
    println(str)
    fw.write(str)
    fw.close()
  }
}
