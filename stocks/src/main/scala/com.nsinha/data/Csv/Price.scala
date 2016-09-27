package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
object Price {
  def apply(s:String): Price = {
    Price(s.toDouble)
  }
}
case class Price(value: Double) extends Ordered[Price] {
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