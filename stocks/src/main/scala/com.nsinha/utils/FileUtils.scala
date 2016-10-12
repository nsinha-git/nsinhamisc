package com.nsinha.utils

import java.io.{File, FileWriter}
import java.nio.file.{FileSystems, Files, Path, StandardCopyOption}

import scala.io.Source

object FileUtils {
  def getParentDirNameFromPath(path: String): String = {
    val dirAndFileMatcher = "(.*)/(.*)".r
    val dirAndFileIter = dirAndFileMatcher.findAllMatchIn(path)
    val dirAndFile = dirAndFileIter.next()
    val dirname = dirAndFile.group(1)
    dirname
  }

  def getOneDeepFileNameFromPath(path: String): String = {
    val dirAndFileMatcher = "(.*)/(.*)".r
    val dirAndFileIter = dirAndFileMatcher.findAllMatchIn(path)
    val dirAndFile = dirAndFileIter.next()
    val dirname = dirAndFile.group(2)
    dirname
  }

  def createDirIfNotPresent(path: String) = {
    val dirname = path
    val dir = new File(dirname)
    if (!dir.exists()) dir.mkdirs()
  }

  def createParentDirIfNotPresent(path: String) = {
    val dirname = getParentDirNameFromPath(path)
    val dir = new File(dirname)
    if (!dir.exists()) dir.mkdirs()
  }

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
      createParentDirIfNotPresent(fileName)
      f.createNewFile()
    }
    Source.fromFile(fileName, "UTF-8")
  }

  def openOrCreateFile(f: File): Source = {
    if (!f.exists) {
      createParentDirIfNotPresent(f.getAbsolutePath)
      f.createNewFile()
    }
    Source.fromFile(f, "UTF-8")
  }

  def moveFileToDestDir(src: String, destDir: String) = {
    val relativeFileName = getOneDeepFileNameFromPath(src)
    val source: Path = FileSystems.getDefault().getPath(getParentDirNameFromPath(src), relativeFileName)
    val target: Path = FileSystems.getDefault().getPath(destDir, relativeFileName)
    createParentDirIfNotPresent(destDir + "/" + relativeFileName)
    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

  def moveFile(src: String, dest: String) = {
    val relativeFileName = getOneDeepFileNameFromPath(src)
    val source: Path = FileSystems.getDefault().getPath(getParentDirNameFromPath(src), relativeFileName)
    val target: Path = FileSystems.getDefault().getPath(getParentDirNameFromPath(dest), getOneDeepFileNameFromPath(dest))
    createParentDirIfNotPresent(dest)
    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

  def writeFile(dest: String, toBeWritten: String) = {
    openOrCreateFile(dest)
    val fw = new FileWriter(dest)
    fw.write(toBeWritten)
    fw.flush()
    fw.close()
  }
}
