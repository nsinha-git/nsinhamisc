package com.nsinha.utils

import java.io.File
import scala.io.Source

object FileUtils {
  def openACsvFile(file: File): Source = {
    try {
      val s= Source.fromFile(file, "UTF-8")
      s.getLines()
      s

    } catch {
      case e: Exception =>
        Source.fromFile(file, "ISO-8859-1")
    }
  }

  def openOrCreateFile(fileName: String): Source = {
    val f = new File(fileName)
    if (!f.exists) {
      val dirAndFileMatcher = "(.*)/(.*)".r
      val dirAndFileIter = dirAndFileMatcher.findAllMatchIn(fileName)
      val dirAndFile = dirAndFileIter.next()
      val dirname = dirAndFile.group(1)
      val dir = new File(dirname)
      if (!dir.exists()) dir.mkdirs()
      f.createNewFile()
    }
    Source.fromFile(fileName, "UTF-8")
  }

  def openOrCreateFile(f: File): Source = {
    if (!f.exists) {
      f.createNewFile()
    }
    Source.fromFile(f, "UTF-8")
  }


}
