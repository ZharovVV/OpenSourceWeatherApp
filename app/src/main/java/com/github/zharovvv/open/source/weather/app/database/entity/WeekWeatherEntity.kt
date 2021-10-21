package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.zharovvv.open.source.weather.app.util.between
import com.github.zharovvv.open.source.weather.app.util.fromJson
import com.github.zharovvv.open.source.weather.app.util.isFresh
import com.github.zharovvv.open.source.weather.app.util.plus
import com.google.gson.GsonBuilder
import java.util.*

@Entity(tableName = "week_weather_table")
@TypeConverters(WeekWeatherItemPojoEntityConverter::class, DateConverter::class)
data class WeekWeatherEntity(
    @PrimaryKey
    override val id: Int,
    val latitude: Float,
    val longitude: Float,
    val updateTime: Date,
    val items: List<WeekWeatherItemPojoEntity>
) : PerishableEntity {
    override val isFresh: Boolean
        get() {
            val firstItemDate = items.first().forecastDate
            val now = Date()
            return updateTime.isFresh(freshPeriodInMinutes = 12 * 60) // 12 часов
                    && now.between(firstItemDate, firstItemDate + 1 of Calendar.DAY_OF_MONTH)
        }
}

data class WeekWeatherItemPojoEntity(
    val forecastDate: Date,
    val iconId: String,
    val maxTemperature: Int,
    val minTemperature: Int
)

class WeekWeatherItemPojoEntityConverter {

    companion object {
        private val GSON = GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create()
    }

    @TypeConverter
    fun pojoToJsonString(items: List<WeekWeatherItemPojoEntity>): String {
        return GSON.toJson(items)
    }

    @TypeConverter
    fun jsonStringToPojo(jsonString: String): List<WeekWeatherItemPojoEntity> {
        return GSON.fromJson(jsonString)
    }
}