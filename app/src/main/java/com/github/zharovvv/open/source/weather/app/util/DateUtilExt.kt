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

