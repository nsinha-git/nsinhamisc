package com.nsinha.impls.Project.JsonCsvProject

import java.io.File

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nsinha.utils.FileUtils

/** Created by nishchaysinha on 10/10/16.
  */
trait ConcatenateJsonUtils {
  def processDirectory(dir : String, outputdir : String, outputfile : String)
  def concatenateJsonString(jsonStrings : List[String]) : String
}

object ConcatenateJsonUtils extends ConcatenateJsonUtils {
  val mapper = new ObjectMapper()
  override def processDirectory(dir : String, outputdirIn : String = "", outputfile : String) = {
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outputdir = if (outputdirIn == "") dir+"/output" else outputdirIn
    val allFilesToConcat : List[File] = srcDir.listFiles().toList
    val destFile = (outputdir+"/"+outputfile)
    val processedDir = (dir+"/processed")
    FileUtils.createParentDirIfNotPresent(destFile)
    FileUtils.createDirIfNotPresent(processedDir)
    var lastTree = try { mapper.readTree(new File(destFile)) } catch { case e : Exception ⇒ null }
    for (f ← allFilesToConcat if ((f.isDirectory == false) && !f.getName.contains("last"))) {
      val rootNode = mapper.readTree(f)
      lastTree = JsonUtils.appendToJsonNode(lastTree, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode))
      //FileUtils.moveFile(f.getAbsolutePath, processedDir + "/" + f.getName)
    }

    if (lastTree != null)
      JsonUtils.writeToFile(new File(destFile), mapper.writerWithDefaultPrettyPrinter().writeValueAsString(lastTree))

  }

  def concatenateJsonString(jsonStrings : List[String]) : String = {
    val concatNode = jsonStrings.foldLeft(null : JsonNode) { (z, el) ⇒
      JsonUtils.appendToJsonNode(z, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(el)))
    }
    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(concatNode)
  }
}
