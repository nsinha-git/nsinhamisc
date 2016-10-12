package com.nsinha.impls.Project.TimeSeries

import java.io.{File, FileReader, FileWriter}

import com.fasterxml.jackson.databind.ObjectMapper
import com.nsinha.utils.{FileUtils, JsonUtils}

/**
  * Created by nishchaysinha on 10/10/16.
  */
trait ConcatenateTickerTimeSeries {
  def processDirectory(dir: String, outputdir: String, outputfile: String)
}


object ConcatenateTickerTimeSeries extends ConcatenateTickerTimeSeries {
  val mapper = new ObjectMapper()
  override def processDirectory(dir: String, outputdirIn: String = "", outputfile: String) = {
    val srcDir = new File(dir)
    assert(srcDir.isDirectory == true)
    val outputdir = if (outputdirIn == "") dir + "/output" else outputdirIn
    val allFilesToConcat: List[File] = srcDir.listFiles().toList
    val destFile = (outputdir + "/" + outputfile)
    val processedDir = (dir + "/processed")
    FileUtils.createParentDirIfNotPresent(destFile)
    FileUtils.createDirIfNotPresent(processedDir)
    for (f <- allFilesToConcat if f.isDirectory == false) {
      val rootNode = mapper.readTree(f)
      JsonUtils.appendToAJsonFile(destFile, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode) )
      FileUtils.moveFile(f.getAbsolutePath, processedDir + "/" + f.getName)

    }
  }
}
