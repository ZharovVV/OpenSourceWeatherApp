package com.github.zharovvv.open.source.weather.app.util

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
    return this.before(endDate) && this.after(startDate)
}

operator fun Date.plus(plusValue: Int): DatePlusOperator = DatePlusOperator(this, plusValue)

class DatePlusOperator(private val date: Date, private val plusValue: Int) {

    infix fun of(calendarField: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(calendarField, plusValue)
        return calendar.time
    }
}

