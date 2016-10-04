package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
object Price {
  def apply(s:String): Price = {
    val ss = s.replaceAll("\\$","")
    Price(ss.toDouble)
  }
}
case class Price(value: Double) extends ValueObject with Ordered[Price] {
  override def create : ValueObject = Price(0)
  override def getValue(): Double = value
  override def setValue (x: String): Price = {
    Price(x)
  }

  def compare(other: Price): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }
}