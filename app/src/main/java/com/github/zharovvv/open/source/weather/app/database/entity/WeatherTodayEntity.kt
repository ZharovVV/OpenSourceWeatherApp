package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

@Entity(tableName = "weather_today_table")
@TypeConverters(DetailedWeatherParamPojoEntityConverter::class, DateConverter::class)
data class WeatherTodayEntity(
    @PrimaryKey
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val time: Date,
    @DrawableRes
    val iconId: Int,
    val description: String,
    val dateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamPojoEntity>
)

data class DetailedWeatherParamPojoEntity(
    val name: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)

class DetailedWeatherParamPojoEntityConverter {

    companion object {
        private val GSON = Gson()
    }

    @TypeConverter
    fun pojoToJsonString(detailedWeatherParamPojoEntities: List<DetailedWeatherParamPojoEntity>): String {
        return GSON.toJson(detailedWeatherParamPojoEntities)
    }

    @TypeConverter
    fun jsonStringToPojo(jsonString: String): List<DetailedWeatherParamPojoEntity> {
        return GSON.fromJson(
            jsonString,
            object : TypeToken<List<DetailedWeatherParamPojoEntity>>() {}.type
        )
    }
}


