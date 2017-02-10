package com.nsinha.impls.Project.TimeSeries.Doubles_ignore

import scala.collection.mutable

/** Created by nishchaysinha on 10/6/16.
  */

class Size(n : Int) {
  def len : Int = n
}
class Size1 extends Size(1)
class Size2 extends Size(2)
class Size3 extends Size(3)

trait DoublesTrait {
  def len : Int
  def get(i : Int) : Double
  def set(i : Int, y : Double)
}

class Doubles[A <: Size](implicit x : A) extends DoublesTrait {
  var map : mutable.Map[Int, Double] = mutable.Map()
  def len : Int = x.len
  implicit val size1 = new Size1
  implicit val size2 = new Size2
  implicit val size3 = new Size3

  override def get(i : Int) : Double = {
    map(i)
  }
  override def set(i : Int, y : Double) = {
    map(i) = y
  }

  def zero() : Doubles[A] = {
    val ll = (List[Double]()).padTo(x.len, 0.0)
    new Doubles[A](ll)
  }

  def this(l : List[Double])(implicit y : A) {
    this
    val z = (l zip Range(0, l.length)).map (y ⇒ this.set(y._2, y._1))
  }

}

object Doubles {
  object Implicits {
    implicit val size1 = new Size1
    implicit val size2 = new Size2
    implicit val size3 = new Size3
  }
}