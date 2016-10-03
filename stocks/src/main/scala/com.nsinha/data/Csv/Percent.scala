package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
object Percent {
  def apply(s:String): Percent= {
    Percent(s.toDouble)
  }
}
case class Percent(value: Double) extends ValueObject with Ordered[Percent] {
  override def create : ValueObject = Percent(0)
  override def getValue(): Double = value
  override def setValue (x: String): Flow = {
    Flow(x)
  }

  def compare(other: Percent): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }
}
