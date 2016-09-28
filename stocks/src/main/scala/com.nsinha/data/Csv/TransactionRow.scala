package com.nsinha.data.Csv

import com.nsinha.data.TypeOfTransaction

/**
*case class GenCsvQuoteRowScottrade(datetimeStart: Long, datetimeEnd: Long, symbol: String, prevprice: Price, endprice: Price, startprice: Price, highprice: Price, lowprice: Price, volume: Volume, companyname: String, percentagechange: Percent) extends CsvQuoteRow()
  * Created by nishchaysinha on 9/27/16.
  * A transaction is defined by the a single purchase and corresponding single sell price. There is no averaging ever in
  * determining a transaction. The polarity change a buy to sell or sell to buy always leads to one or more transaction.
  * Transaction will be on longest duration stocks held or shorted.
  *
  */
case class TransactionRow(symbol: String, dateTime: Long, volume: Volume, initPrice: Price, endPrice: Price, diff: Option[Flow], typeOfTransaction: TypeOfTransaction)

