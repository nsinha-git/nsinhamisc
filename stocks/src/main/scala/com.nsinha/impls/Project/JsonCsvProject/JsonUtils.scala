package com.nsinha.impls.Project.JsonCsvProject

import java.io.{File, FileWriter}

import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeType}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

import scala.collection.convert.wrapAsScala._

/**
  * Created by nishchaysinha on 10/4/16.
  */
object JsonUtils {
  val mapper: ObjectMapper = new ObjectMapper()

  def appendToAJsonFile(fileName: String, jsonString: String) = {
    val file = new File (fileName)
    val prevTree: JsonNode = try { mapper.readTree(file)} catch { case e: Exception => null}
    val node = appendToJsonNode(prevTree, jsonString)
    val opStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    writeToFile(file, opStr)
  }

  def writeToFile(file: File, opStr: String) = {
    val fw = new FileWriter(file)
    fw.write(opStr)
    fw.flush()
    fw.close
  }

  def appendToJsonNode(prevTree: JsonNode, jsonString: String): JsonNode = {
    val curTree: JsonNode = mapper.readTree(jsonString)
    if (prevTree != null && prevTree.isMissingNode == false) mergeNodes(prevTree, curTree) else curTree

  }

  def addNodesToArrayNode(rootNode: ArrayNode, iterator: Iterator[JsonNode]) = {
    for (elem <- iterator) {
        rootNode.add(elem)
    }
  }

  def mergeNodes(prevTree: JsonNode, curTree: JsonNode): JsonNode = {
    val newNode = mapper.getNodeFactory.arrayNode()
    (prevTree.getNodeType, curTree.getNodeType) match {
      case (JsonNodeType.ARRAY, JsonNodeType.ARRAY) =>
        addNodesToArrayNode(newNode, prevTree.elements())
        addNodesToArrayNode(newNode, curTree.elements())

      case (JsonNodeType.ARRAY, JsonNodeType.OBJECT) =>
        addNodesToArrayNode(newNode, prevTree.elements())
        addNodesToArrayNode(newNode, Iterator(curTree))
      case (JsonNodeType.OBJECT, JsonNodeType.OBJECT) =>
        addNodesToArrayNode(newNode, Iterator(prevTree))
        addNodesToArrayNode(newNode, Iterator(curTree))
      case (_, _) => assert(false)
        null
    }
    newNode
  }


  def main(args: Array[String]) {
    val n1 = """
      [{"name":"a" },{"name": "b"}]
      """
    val n2 = """
      [{"name":"c" },{"name": "d"}]
      """

    val nExpected =
      """
      [{"name":"a" },{"name": "b"}, {"name":"c"},{"name":"d"}]
      """
    val t1: JsonNode = mapper.readTree(n1)
    val t2: JsonNode = mapper.readTree(n2)

    val t3 = mergeNodes(t1,t2)

    val opStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(t3);
    println(opStr)

  }

  def writeCsvFile(jsonFileName: String) = {
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }
}
