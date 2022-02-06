package com.github.zharovvv.open.source.weather.app.models.data.local

import androidx.room.*
import androidx.room.Entity
import com.github.zharovvv.open.source.weather.app.models.domain.TimeIndicator
import com.github.zharovvv.open.source.weather.app.util.between
import com.github.zharovvv.open.source.weather.app.util.fromJson
import com.github.zharovvv.open.source.weather.app.util.isFresh
import com.github.zharovvv.open.source.weather.app.util.plus
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "hourly_weather_table")
@TypeConverters(HourlyWeatherItemPojoEntityConverter::class, DateConverter::class)
data class HourlyWeatherEntity(
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
    @SerializedName(value = "now")
    val now: Boolean,
    @SerializedName(value = "timeIndicator")
    val timeIndicator: TimeIndicator,
    @SerializedName(value = "time")
    val time: Date,
    @SerializedName(value = "iconId")
    val iconId: String,
    @SerializedName(value = "value")
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