package com.github.zharovvv.open.source.weather.app.models.data.local

import androidx.room.*
import androidx.room.Entity
import com.github.zharovvv.open.source.weather.app.util.fromJson
import com.github.zharovvv.open.source.weather.app.util.isFresh
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "weather_today_table")
@TypeConverters(DetailedWeatherParamPojoEntityConverter::class, DateConverter::class)
data class WeatherTodayEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Float,
    @ColumnInfo(name = "longitude")
    val longitude: Float,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "country_name")
    val countryName: String,
    @ColumnInfo(name = "update_time")
    val updateTime: Date,
    @ColumnInfo(name = "icon_id")
    val iconId: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date_string")
    val dateString: String,
    @ColumnInfo(name = "short_date_string")
    val shortDateString: String,
    @ColumnInfo(name = "temperature")
    val temperature: String,
    @ColumnInfo(name = "detailed_weather_params")
    val detailedWeatherParams: List<DetailedWeatherParamPojoEntity>
) : PerishableEntity {
    override val isFresh: Boolean
        get() = updateTime.isFresh(freshPeriodInMinutes = 2)
}

data class DetailedWeatherParamPojoEntity(
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "iconId")
    val iconId: String,
    @SerializedName(value = "value")
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


