package com.github.zharovvv.open.source.weather.app.util

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import java.util.*

/**
 * Проверяет, что дата (объект-получатель [this]) является "свежей", т.е.
 * Текущая дата (now) находится в диапазоне [this]..[this]+[freshPeriodInMinutes]
 */
fun Date.isFresh(freshPeriodInMinutes: Int): Boolean {
    val expired = Calendar.getInstance()
    expired.time = this
    expired.add(Calendar.MINUTE, freshPeriodInMinutes)
    val now = Calendar.getInstance()
    return now.before(expired)
}

fun Date.between(startDate: Date, endDate: Date): Boolean {
    return this >= startDate && this < endDate
}

/**
 * Устанавливает для даты время 00:00:00
 */
fun Date.setZeroTime(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.setZeroTime()
    return calendar.time
}

/**
 * DSL for Date
 */
operator fun Date.plus(plusValue: Int): DatePlusOperator = DatePlusOperator(this, plusValue)

class DatePlusOperator(private val date: Date, private val plusValue: Int) {

    infix fun of(calendarField: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(calendarField, plusValue)
        return calendar.time
    }
}

enum class TimeIndicator(val description: String) {
    TODAY(OpenSourceWeatherApp.appContext.getString(R.string.today)),
    TOMORROW(OpenSourceWeatherApp.appContext.getString(R.string.tomorrow)),
    DAY_AFTER_TOMORROW(OpenSourceWeatherApp.appContext.getString(R.string.day_after_tomorrow)),
    FUTURE(OpenSourceWeatherApp.appContext.getString(R.string.empty))
}

/**
 * Определяет [TimeIndicator] даты.
 * Например если сегодня - 01.02.1970, а дата, для которой будет вызвано данное свойство - 02.02.1970,
 * то свойство вернет значение [TimeIndicator.TOMORROW].
 */
val Date.timeIndicator: TimeIndicator
    get() {
        val checkedDate = Calendar.getInstance()
        checkedDate.time = this
        val tomorrow = tomorrow()
        val dayAfterTomorrow = dayAfterTomorrow()
        val future = future()
        //TODAY (DATE < TOMORROW)
        if (checkedDate.before(tomorrow)) {
            return TimeIndicator.TODAY
        }
        //TOMORROW (TOMORROW <= DATE < DAY AFTER TOMORROW)
        if (checkedDate.afterInclusive(tomorrow) && checkedDate.before(dayAfterTomorrow)) {
            return TimeIndicator.TOMORROW
        }
        //DAY AFTER TOMORROW (DAY AFTER TOMORROW <= DATE < FUTURE)
        if (checkedDate.afterInclusive(dayAfterTomorrow) && checkedDate.before(future)) {
            return TimeIndicator.DAY_AFTER_TOMORROW
        }
        //else FUTURE
        return TimeIndicator.FUTURE
    }

/**
 * Возвращает объект [Calendar], с датой и временем начала завтрашнего дня.
 */
fun tomorrow(): Calendar {
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DATE, 1)
    tomorrow.setZeroTime()
    return tomorrow
}

/**
 * Возвращает объект [Calendar], с датой и временем начала "послезавтра".
 */
fun dayAfterTomorrow(): Calendar {
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DATE, 2)
    tomorrow.setZeroTime()
    return tomorrow
}

/**
 * Возвращает объект [Calendar], с датой и временем начала дня, который начнется через 3 дня.
 */
fun future(): Calendar {
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DATE, 3)
    tomorrow.setZeroTime()
    return tomorrow
}

private fun Calendar.setZeroTime() {
    this.set(Calendar.HOUR, 0)
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
}

fun Calendar.afterInclusive(another: Calendar): Boolean {
    return compareTo(another) >= 0
}

