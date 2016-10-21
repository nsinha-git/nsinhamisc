package com.nsinha.impls.Project.TimeSeries.Analysis

/**
  * Created by nishchaysinha on 10/12/16.
  */

trait NormalizationPolicy
case class NoNormalizationPolicy() extends NormalizationPolicy
case class Jan1is1NormalizationPolicy() extends NormalizationPolicy

object AccumulateTicker {

  def accumulateTickerForYears(range: Range, ticker: String, tickerList: List[String], normalizationPolicy: NormalizationPolicy, inParentDir: String, outDir: String) = {
    //dump a csv file with ohlcv for the ticker and the ticker list with the normalization policy


  }

}
