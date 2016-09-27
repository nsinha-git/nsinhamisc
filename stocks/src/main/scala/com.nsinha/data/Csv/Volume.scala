package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
object Volume{
  def apply(s:String): Volume= {
    Volume(s.toDouble)
  }
}
case class Volume(value: Double) extends Ordered[Volume] {
   def compare(other: Volume): Int = {
    if (value < other.value) {
      -1
    } else if ( value == other.value) {
      0
    } else {
      1
    }
  }

}