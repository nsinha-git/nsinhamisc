package com.nsinha.utils

/**
  * Created by nishchaysinha on 9/29/16.
  */
object StringUtils {

  def convertStringToSpecifiedFormat(s: String, format: String): AnyVal = {
    format.toLowerCase match {
      case "boolean" | "bool" => s.toBoolean
      case "float" => s.toFloat
      case "double" => s.toDouble
      case "int" | "integer" => s.toInt
      case "Long" => s.toLong
    }
  }

  def extractPrintable(s: String): String = {
    val res = new StringBuilder
    for (c <- s){
      if(c.toInt >= 32 && c.toInt<127) res.+(c)
    }
    res.toString()
  }

  def countThisCharInString(s: String, c: Char): Int = {
    s.foldLeft(0)((Z,x) => if (x == c) Z+1 else Z )
  }

}
