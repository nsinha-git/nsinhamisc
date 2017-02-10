package com.nsinha.data.Csv

/** Created by nishchaysinha on 9/26/16.
  */
sealed trait Execution
case class Bought() extends Execution
case class Sold() extends Execution

object Execution {
  def apply(s : String) : Execution = {
    s.toLowerCase match {
      case "bought"          ⇒ Bought()
      case "bought to cover" ⇒ Bought()
      case "sold"            ⇒ Sold()
      case "sold short"      ⇒ Sold()
    }
  }
}
