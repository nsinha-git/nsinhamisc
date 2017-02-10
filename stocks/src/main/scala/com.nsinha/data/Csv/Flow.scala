package com.nsinha.data.Csv

/** Created by nishchaysinha on 9/27/16.
  */

object Flow {
  def apply(x : String) : Flow = {
    Flow(x.toDouble)
  }
}
case class Flow(value : Double) extends ValueObject {
  override def create : ValueObject = Flow(0)
  override def getValue() : Double = value

  override def setValue(x : String) : Flow = {
    Flow(x)
  }
}
