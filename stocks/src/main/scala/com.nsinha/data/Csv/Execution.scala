package com.nsinha.data.Csv

/**
  * Created by nishchaysinha on 9/26/16.
  */
sealed trait Execution
case class  Bought() extends Execution
case class  Sold() extends Execution
