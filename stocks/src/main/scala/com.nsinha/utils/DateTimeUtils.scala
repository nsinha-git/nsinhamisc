package com.nsinha.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

import org.joda.time.DateTime
//import org.joda.time.format.DateTimeFormat

/**
  * Created by nishchaysinha on 9/24/16.
  */
object DateTimeUtils extends Loggable {

  def sort(dates: List[DateTime]): List[DateTime] = {
    val sortedDates = dates.sortWith((x,y) => {
      if (x.isAfter(y)) false else true
    })
    sortedDates
  }

  def toStartBusinessHourOfDay(date: DateTime): DateTime = {
    val startDayTime = date.withTimeAtStartOfDay()
    startDayTime.plusHours(13).plusMinutes(30)
  }


  def toEndBusinessHourOfDay(date: DateTime): DateTime = {
    val startDayTime = date.withTimeAtStartOfDay()
    startDayTime.plusHours(20)
  }

  def toLastBusinessDayStartHour(date: DateTime): DateTime = {
    val diff = {
      val x = (date.dayOfWeek().get() - 1)
      if (x == 0) 3 else 1
    }
    val lastDay = date.minusDays(diff)
    toStartBusinessHourOfDay(lastDay)
  }

  def toLastBusinessDayEndHour(date: DateTime): DateTime = {
    val diff = {
      val x = (date.dayOfWeek().get() - 1)
      if (x == 0) 3 else 1
    }
    val lastDay = date.minusDays(diff)
    toEndBusinessHourOfDay(lastDay)
  }

  def toStartBusinessHourOfYear(date: DateTime): DateTime = {
    val startDayTimeOfYear = date.withDayOfYear(2).withTimeAtStartOfDay()
    val toAddForBusinessDay = if (startDayTimeOfYear.dayOfWeek().get() >= 6) {
      7 - startDayTimeOfYear.dayOfWeek().get() + 1
    } else 0
    startDayTimeOfYear.plusDays(toAddForBusinessDay).withTimeAtStartOfDay().plusHours(13).plusMinutes(30)

  }

  def parseDateTime(date: String, time: String, format: String = "MM/dd/yyyy hh:mm:ss a") : DateTime = {
    val dtf = new SimpleDateFormat(format)
    val dateTime = dtf.parse(date + " " + time)
    dateTime.getTime
    val dt = new DateTime(dateTime.getTime)
    logger.debug(s"datetime = $date $time o/p=$dateTime ${dt.getMillis}")
    dt
  }
}
