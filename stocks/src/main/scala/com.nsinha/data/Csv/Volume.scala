package com.nsinha.data.Csv

/** Created by nishchaysinha on 9/26/16.
  */
object Volume {
  def apply(s : String) : Volume = {
    Volume(s.toDouble)
  }

  def -(x : Volume, y : Volume) : Volume = {
    Volume(x.value - y.value)
  }

  def >(x : Volume, y : Volume) : Boolean = {
    x.value > y.value
  }

}
case class Volume(value : Double) extends ValueObject with Ordered[Volume] {
  override def create : ValueObject = Volume(0)
  override def getValue() : Double = value
  override def setValue(x : String) : Volume = {
    Volume(x)
  }

  def compare(other : Volume) : Int = {
    if (value < other.value) {
      -1
    }
    else if (value == other.value) {
      0
    }
    else {
      1
    }
  }
}