package com.github.zharovvv.open.source.weather.app.models.data.local

import androidx.room.*
import androidx.room.Entity
import com.github.zharovvv.open.source.weather.app.util.between
import com.github.zharovvv.open.source.weather.app.util.fromJson
import com.github.zharovvv.open.source.weather.app.util.isFresh
import com.github.zharovvv.open.source.weather.app.util.plus
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "week_weather_table")
@TypeConverters(WeekWeatherItemPojoEntityConverter::class, DateConverter::class)
data class WeekWeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Float,
    @ColumnInfo(name = "longitude")
    val longitude: Float,
    @ColumnInfo(name = "update_time")
    val updateTime: Date,
    @ColumnInfo(name = "items")
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
    @SerializedName(value = "forecastDate")
    val forecastDate: Date,
    @SerializedName(value = "iconId")
    val iconId: String,
    @SerializedName(value = "maxTemperature")
    val maxTemperature: Int,
    @SerializedName(value = "minTemperature")
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