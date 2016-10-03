package com.nsinha.data.Project

import com.nsinha.data.Csv.ValueObject

/**
  * Created by nishchaysinha on 10/2/16.
  */
trait YearlyQuoteAnalysisProject extends Project {
  def createTimeSeries[A <: ValueObject](key: String, axis: String, canBuildA: A): String
}
