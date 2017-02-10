package com.nsinha.impls.Project.JsonCsvProject

import java.io.FileWriter

/** Created by nishchaysinha on 9/30/16.
  */
object CsvUtils {

  def writeCsvHeaderAndRows(rows : List[List[(String, String)]], cols : Set[String]) : String = {
    //convert List[list] into List{map]

    val rowsMap : List[Map[String, String]] = rows map { r ⇒ r toMap }
    val allCols : List[String] = rowsMap map { rowMap ⇒
      val colVals = cols.toList map { col ⇒
        rowMap.get(col) match {
          case None    ⇒ "NA"
          case Some(x) ⇒ x
        }
      }
      colVals.mkString(",")
    }
    val header = cols.toList.mkString(",")
    val data = allCols.mkString("\n")
    header+"\n"+data
  }

  def writeCsvHeaderAndRows(rows : List[List[(String, String)]], cols : List[String]) : String = {
    //convert List[list] into List{map]

    val rowsMap : List[Map[String, String]] = rows map { r ⇒ r toMap }
    val allCols : List[String] = rowsMap map { rowMap ⇒
      val colVals = cols map { col ⇒
        rowMap.get(col) match {
          case None    ⇒ "NA"
          case Some(x) ⇒ x
        }
      }
      colVals.mkString(",")
    }
    val header = cols.mkString(",")
    val data = allCols.mkString("\n")
    header+"\n"+data
  }

}
