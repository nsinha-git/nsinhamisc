package com.nsinha.data

import com.nsinha.data.Csv.{Bought, Execution, Sold}

/**
  * Created by nishchaysinha on 9/27/16.
  */
trait TypeOfTransaction

case class Short() extends TypeOfTransaction
case class Long() extends TypeOfTransaction

object TypeOfTransaction {
  def apply(s: Execution): TypeOfTransaction = {
    s match {
      case Bought() => Long()
      case Sold() => Short()
    }
  }
  def apply(s: String): TypeOfTransaction = {
    s match {
      case "short" => Short()
      case "long" => Long()
    }
  }
}
