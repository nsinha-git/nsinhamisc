package com.nsinha.impls.Project.Orders

import java.io.{File, FileWriter}

import com.nsinha.data.Csv._
import com.nsinha.data.Csv.generated.GenCsvOrderRowScottrade
import com.nsinha.data.Project.CsvOrderScottradeProject
import com.nsinha.data.TypeOfTransaction
import com.nsinha.utils.{DateTimeUtils, Loggable}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.collection.mutable
import scala.io.Source

/**
  * class GenCsvOrderRowScottrade (datetime: Long, symbol: String, executionPrice: Price, executedVolume: Volume,  typeOfExecution: Execution) extends  CsvOrderRow
  * Created by nishchaysinha on 9/26/16.
  */
class CsvOrderScottradeProjectImpl(modelFilePath: String, dumpFilePath: String) extends CsvOrderScottradeProject with Loggable {

  val modelFile = new File(modelFilePath)
  val dumpFile = new File(dumpFilePath)
  val csvModel = readModelMap(modelFile)
  val rows: List[GenCsvOrderRowScottrade] = readCsv(dumpFile, csvModel)
  val transactions: Map[String, List[TransactionRow]]= createTransactions
  val unfinishedTransactions: Map[String, List[TransactionRow]]= createUnfinishedTransactions(transactions)


  override def readModelMap(file: File): CsvModel = {
    val symbol = "Symbol"
    val date = "Trade Date"
    val time = "Time Completed"
    val executionPrice  = "Order Price"
    val typeOfExecution = "Action"
    val executedVolume = "Qty"
    val map = Map(date -> "date", time -> "time", executionPrice -> "executionPrice",
      executedVolume -> "executedVolume", typeOfExecution -> "typeOfExecution", symbol -> "symbol" )
    CsvModel(map)
  }
  override def readCsv(file: File, csvModel: CsvModel): List[GenCsvOrderRowScottrade] = {
    val source = Source.fromFile(file, "UTF-8")
    var i = 0
    var start = false
    var cols: Map[String, Int] = null
    var result: List[GenCsvOrderRowScottrade] = List()
    for (line <- source.getLines()) {
      start match {
        case false => if (line.length > 1) {
          cols = createColsList(line, csvModel, "")
        }
        case true =>
          val rowCols: Map[String, String] =  extractRowCols(line, cols)
          result = result.+:(mapToCsvRow(rowCols, ""))
      }
      if (cols != null){
        start = true
      }
      i = i + 1
    }
    result


  }

  def mapToCsvRow (rowCols: Map[String,String], prefix: String): GenCsvOrderRowScottrade = {
    val date  = rowCols("date")
    val time  = rowCols("time")
    val dateTime = DateTimeUtils.parseDateTime(date, time)
    GenCsvOrderRowScottrade(dateTime.getMillis, rowCols("symbol"), executionPrice = Price(rowCols("executionPrice")), executedVolume = Volume(rowCols("executedVolume")), typeOfExecution = Execution(rowCols("typeOfExecution")))
  }

  def createTransactions: Map[String, List[TransactionRow]] = {
    val mapOfSymbolsToListOfOrders: Map[String, List[GenCsvOrderRowScottrade]] = rows.foldLeft(Map[String, List[GenCsvOrderRowScottrade]]()) { (map, row) =>
      val l = map.get(row.getKey()) match {
        case Some(list) => list.::(row)
        case None => List(row)
      }
      map ++ Map(row.getKey() -> l)
    }
    //convert every list to  a single priority queue
    val mapOfSymbolsToPriorityQsOfOrders: Map[String, mutable.PriorityQueue[GenCsvOrderRowScottrade]] = mapOfSymbolsToListOfOrders.foldLeft(Map[String, mutable.PriorityQueue[GenCsvOrderRowScottrade]]()){ (b, kv) =>
      b + (kv._1 -> transformIntoPriorityQ(kv._2))
    }
    val mp: Map[String, List[TransactionRow]] = processAsTransaction(mapOfSymbolsToPriorityQsOfOrders)
    mp
  }

  override def dumpPerformance(file: String) = {
    implicit val format = DefaultFormats
    val fw = new FileWriter(file)
    fw.write(writePretty(transactions))
  }

  override def dumpPerformanceMostRecentFirst(file: String) = {

  }

  override def dumpPerformanceCurrentHolds(file: String) = {
    implicit val format = DefaultFormats
    val fw = new FileWriter(file)
    fw.write(writePretty(unfinishedTransactions))
  }

  private def createUnfinishedTransactions(trans: Map[String, List[TransactionRow]]): Map[String, List[TransactionRow]] = {
    //all those transactions that  have unfinished trans
    val unfinishedTrans = findUnfinishedTrans()
    unfinishedTrans
  }

  private def findUnfinishedTrans(): Map[String, List[TransactionRow]] = {
    transactions map {t =>
      t._1 -> findUnfinishedTransFromTransList(t._2)
    }
  }

  private def findUnfinishedTransFromTransList(transList: List[TransactionRow]): List[TransactionRow] = {
    transList filter ((x) => {
      if(x.diff == None) true else false
    })
  }


  private def processAsTransaction(map: Map[String, mutable.PriorityQueue[GenCsvOrderRowScottrade]]): Map[String, List[TransactionRow]] = {
    `map` map { x =>
      val symbol = x._1
      val queOrders = mutable.PriorityQueue[GenCsvOrderRowScottrade]()(GenCsvOrderRowScottrade.ordering())
      val queTrans = mutable.Queue[TransactionRow]()
      val allTransForSymbol = createAllTransactions(x._2, queOrders, queTrans)
      symbol -> allTransForSymbol
    }
  }

  private def createTransaction(currentAdj: GenCsvOrderRowScottrade, mostRecent: GenCsvOrderRowScottrade): TransactionRow = {
    val price1 = currentAdj.executionPrice
    val price2 = mostRecent.executionPrice
    val trans = TransactionRow(symbol = mostRecent.symbol, dateTime = mostRecent.datetime,
      volume = Volume(Math.min(mostRecent.executedVolume.value, currentAdj.executedVolume.value)), initPrice = price1, endPrice = price2,
      diff = Option(Flow(mostRecent.executedVolume.value * (price2.value - price1.value))), typeOfTransaction = TypeOfTransaction(currentAdj.typeOfExecution))

    logger.debug(s"transaction created = ${trans}")
    trans
  }

  private def createTransaction(currentAdj: GenCsvOrderRowScottrade): TransactionRow = {
    val price1 = currentAdj.executionPrice
    val price2 = Price(0)
    TransactionRow(symbol = currentAdj.symbol, dateTime = currentAdj.datetime,
      volume = currentAdj.executedVolume, initPrice = price1, endPrice = price2,
      diff = None, typeOfTransaction = TypeOfTransaction(currentAdj.typeOfExecution))
  }

  private def createAllTransactions(preExistingOrderList: mutable.PriorityQueue[GenCsvOrderRowScottrade], queueOrders: mutable.PriorityQueue[GenCsvOrderRowScottrade], queueTransactions: mutable.Queue[TransactionRow]): List[TransactionRow]  = {
    /*
    1. invariants: the order que always contains all sell , all buys or empty que. This is becoz the different polarity is always adjusted
    against each other before any later action on queue. We eschew a case when a holder holds both the short and long position on same stock
    2. nextOrder if opposite polarity (sell vs buy. empty is not opposite to anyone.) then adjust(adjust is always possible as you can not
    sell more than you bought or cover more than you shorted).Adjust creates a transaction. add new trans to queTrans
    3. nextOrder  if same polarity add to orderQue.
    4. At exit, empty the orderQue and report them as separate transactions and flow as None.
    5. Return the transactionsQue
     */

    /**
      * for a given adjustedOrder either add it to same polarity que
      * or adjust it. adjust happens by repeatedly dequeing from queOrders.(will never fail by  invariant 1)
      */
    val preExistingOrderListDequed: List[GenCsvOrderRowScottrade] = preExistingOrderList.dequeueAll
    for (adjustedOrderOrig <- preExistingOrderListDequed) {
      logger.debug(s"adjustedOrderOrig = ${adjustedOrderOrig.toString}")
      var adjustedOrder = adjustedOrderOrig
      val orderQuePolarity = PolarityOfOrder.getPolarityOfOrderQue(queueOrders)
      val adjustedOrderPolarity = PolarityOfOrder.getPolarityOfOrder(adjustedOrder)

      if (PolarityOfOrder.isOppositePolarity(adjustedOrderPolarity, orderQuePolarity)) {
        assert(queueOrders.nonEmpty)
        var cond = true
        do {
          assert(queueOrders.nonEmpty)
          logger.debug(s"queueOrders= ${queueOrders}")
          var currentAdjusting: GenCsvOrderRowScottrade = queueOrders.dequeue()
          logger.debug(s"currentAdjusting = ${currentAdjusting.toString}")
          val excess: Volume = Volume(currentAdjusting.executedVolume.value - adjustedOrder.executedVolume.value)
          logger.debug(s"excess= ${excess}")
          if (excess.value > 0) {
            //the transaction is over we fully adjusted a single current order
            val trans = createTransaction(currentAdjusting, adjustedOrder)
            queueTransactions.enqueue(trans)
            //we need to enque back the leftover
            val leftoverTrade = currentAdjusting.copy(executedVolume = excess)
            queueOrders.enqueue(leftoverTrade) //goes back to front
            cond = false
            currentAdjusting = null
          } else if (excess.value == 0) {
            val trans = createTransaction(currentAdjusting, adjustedOrder)
            queueTransactions.enqueue(trans)
            cond = false
            currentAdjusting = null
          } else {
            //the last enqueued order was not enough to fulfill the current order so dequeue 1 more
            val trans = createTransaction(currentAdjusting, adjustedOrder)
            queueTransactions.enqueue(trans)
            adjustedOrder = adjustedOrder.copy(executedVolume = Volume(-excess.value))
            cond = true
          }
        } while (cond)
      } else {
        queueOrders.enqueue(adjustedOrderOrig)
      }
    }

    //at this stage we may still have a non empty queueOrders
    queueOrders map {x => queueTransactions.enqueue(createTransaction(x))}
    queueTransactions.toList
  }



  private def transformIntoPriorityQ(list :List[GenCsvOrderRowScottrade]): mutable.PriorityQueue[GenCsvOrderRowScottrade] = {
    val priorityQueue = mutable.PriorityQueue[GenCsvOrderRowScottrade]()(GenCsvOrderRowScottrade.ordering())
    list map ( l => priorityQueue.enqueue(l))
    priorityQueue
  }
}
