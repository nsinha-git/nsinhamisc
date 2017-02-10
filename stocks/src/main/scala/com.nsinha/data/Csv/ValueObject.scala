package com.nsinha.data.Csv

/** Created by nishchaysinha on 10/2/16.
  */

object ValueObject {
  def setValue[A <: ValueObject](x : String, canBuild : A) : ValueObject = {
    canBuild.setValue(x)
  }
}

trait ValueObject {
  def create : ValueObject
  def getValue() : Double
  def setValue(x : String) : ValueObject
}
