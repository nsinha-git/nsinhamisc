package com.nsinha.impls.Project.Orders

import com.nsinha.data.Csv.Bought
import com.nsinha.data.Csv.generated.GenCsvOrderRowScottrade

import scala.collection.mutable

/**
  * Created by nishchaysinha on 9/27/16.
  */
trait PolarityOfOrder
object PolarityOfOrder {
  def isOppositePolarity(x: PolarityOfOrder, y: PolarityOfOrder): Boolean = {
    (x, y) match {
      case (BoughtPolarityOfOrder(), SoldPolarityOfOrder()) => true
      case (SoldPolarityOfOrder(), BoughtPolarityOfOrder()) => true
      case _ => false
    }
  }

  def getPolarityOfOrder(order: GenCsvOrderRowScottrade): PolarityOfOrder = {
    if (order.typeOfExecution == Bought()) BoughtPolarityOfOrder() else SoldPolarityOfOrder()
  }
  def getPolarityOfOrderQue(queue: mutable.PriorityQueue[GenCsvOrderRowScottrade]): PolarityOfOrder = {
    if (queue.isEmpty) {
      NoPolarityOfOrder()
    } else {
      if (queue.head.typeOfExecution == Bought()) BoughtPolarityOfOrder() else SoldPolarityOfOrder()
    }
  }
}

case  class BoughtPolarityOfOrder() extends PolarityOfOrder
case  class SoldPolarityOfOrder() extends PolarityOfOrder
case  class NoPolarityOfOrder() extends PolarityOfOrder
