package com.nsinha.utils

import java.io.{File, FileWriter}

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, JsonNodeType}
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
    val curTree: JsonNode = mapper.readTree(jsonString)
    val node = if (prevTree != null && prevTree.isMissingNode == false) mergeNodes(prevTree, curTree) else curTree
    val opStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    val fw = new FileWriter(file, true)
    fw.write(opStr)
    fw.flush()
    fw.close
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
}
