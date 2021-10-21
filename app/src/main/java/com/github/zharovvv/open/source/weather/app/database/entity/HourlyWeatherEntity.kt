package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.zharovvv.open.source.weather.app.util.*
import com.google.gson.GsonBuilder
import java.util.*

@Entity(tableName = "hourly_weather_table")
@TypeConverters(HourlyWeatherItemPojoEntityConverter::class, DateConverter::class)
data class HourlyWeatherEntity(
    @PrimaryKey
    override val id: Int,
    val latitude: Float,
    val longitude: Float,
    val updateTime: Date,
    val items: List<HourlyWeatherItemPojoEntity>
) : PerishableEntity {
    override val isFresh: Boolean
        get() {
            val firstItemTime = items.first().time
            val now = Date()
            return updateTime.isFresh(freshPeriodInMinutes = 60)
                    && now.between(firstItemTime, firstItemTime + 1 of Calendar.HOUR)
        }

}

data class HourlyWeatherItemPojoEntity(
    val now: Boolean,
    val timeIndicator: TimeIndicator,
    val time: Date,
    val iconId: String,
    val value: String
)

class HourlyWeatherItemPojoEntityConverter {

    companion object {
        private val GSON = GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create()
    }

    @TypeConverter
    fun pojoToJsonString(items: List<HourlyWeatherItemPojoEntity>): String {
        return GSON.toJson(items)
    }

    @TypeConverter
    fun jsonStringToPojo(jsonString: String): List<HourlyWeatherItemPojoEntity> {
        return GSON.fromJson(jsonString)
    }
}