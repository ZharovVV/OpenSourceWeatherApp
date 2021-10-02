package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun dateToLong(date: Date): Long = date.time

    @TypeConverter
    fun longToDate(timeLong: Long): Date = Date(timeLong)
}