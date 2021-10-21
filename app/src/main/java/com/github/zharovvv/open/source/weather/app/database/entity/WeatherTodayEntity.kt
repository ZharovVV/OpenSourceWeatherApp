package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.zharovvv.open.source.weather.app.util.fromJson
import com.github.zharovvv.open.source.weather.app.util.isFresh
import com.google.gson.Gson
import java.util.*

@Entity(tableName = "weather_today_table")
@TypeConverters(DetailedWeatherParamPojoEntityConverter::class, DateConverter::class)
data class WeatherTodayEntity(
    @PrimaryKey
    override val id: Int,
    val latitude: Float,
    val longitude: Float,
    val updateTime: Date,
    val iconId: String,
    val description: String,
    val dateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamPojoEntity>
) : PerishableEntity {
    override val isFresh: Boolean
        get() = updateTime.isFresh(freshPeriodInMinutes = 2)
}

data class DetailedWeatherParamPojoEntity(
    val name: String,
    val iconId: String,
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
        return GSON.fromJson(jsonString)
    }
}


