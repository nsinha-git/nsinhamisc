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

}
