package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

@Entity(tableName = "hourly_weather_table")
@TypeConverters(HourlyWeatherItemPojoEntityConverter::class, DateConverter::class)
data class HourlyWeatherEntity(
    @PrimaryKey
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val updateTime: Date,
    val items: List<HourlyWeatherItemPojoEntity>
)

data class HourlyWeatherItemPojoEntity(
    val now: Boolean,
    val time: Date,
    @DrawableRes
    val iconId: Int,
    val value: String
)

class HourlyWeatherItemPojoEntityConverter {

    companion object {
        private val GSON = Gson()
    }

    @TypeConverter
    fun pojoToJsonString(items: List<HourlyWeatherItemPojoEntity>): String {
        return GSON.toJson(items)
    }

    @TypeConverter
    fun jsonStringToPojo(jsonString: String): List<HourlyWeatherItemPojoEntity> {
        return GSON.fromJson(
            jsonString,
            object : TypeToken<List<HourlyWeatherItemPojoEntity>>() {}.type
        )
    }
}