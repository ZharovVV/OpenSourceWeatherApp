package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherItemPojoEntity
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherItemModel
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.network.dto.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.timeIndicator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class HourlyWeatherConverter {

    companion object {
        private val ICONS_MAP: Map<String, Int> = hashMapOf(
            "01d" to R.drawable.ic_clear_sky_24,
            "02d" to R.drawable.ic_few_clouds_24,
            "03d" to R.drawable.ic_clouds_24,
            "04d" to R.drawable.ic_clouds_24,
            "09d" to R.drawable.ic_rain_24,
            "10d" to R.drawable.ic_rain_24,
            "11d" to R.drawable.ic_thunderstorm_24,
            "13d" to R.drawable.ic_snow_24,
            "50d" to R.drawable.ic_clouds_24,

            "01n" to R.drawable.ic_clear_sky_24,
            "02n" to R.drawable.ic_few_clouds_24,
            "03n" to R.drawable.ic_clouds_24,
            "04n" to R.drawable.ic_clouds_24,
            "09n" to R.drawable.ic_rain_24,
            "10n" to R.drawable.ic_rain_24,
            "11n" to R.drawable.ic_thunderstorm_24,
            "13n" to R.drawable.ic_snow_24,
            "50n" to R.drawable.ic_clouds_24
        )
        private val ICONS_MAP_DARK: Map<String, Int> = hashMapOf(
            "01d" to R.drawable.ic_clear_sky_24,
            "02d" to R.drawable.ic_few_clouds_24_dark,
            "03d" to R.drawable.ic_clouds_24_dark,
            "04d" to R.drawable.ic_clouds_24_dark,
            "09d" to R.drawable.ic_rain_24_dark,
            "10d" to R.drawable.ic_rain_24_dark,
            "11d" to R.drawable.ic_thunderstorm_24_dark,
            "13d" to R.drawable.ic_snow_24_dark,
            "50d" to R.drawable.ic_clouds_24_dark,

            "01n" to R.drawable.ic_clear_sky_24,
            "02n" to R.drawable.ic_few_clouds_24_dark,
            "03n" to R.drawable.ic_clouds_24_dark,
            "04n" to R.drawable.ic_clouds_24_dark,
            "09n" to R.drawable.ic_rain_24_dark,
            "10n" to R.drawable.ic_rain_24_dark,
            "11n" to R.drawable.ic_thunderstorm_24_dark,
            "13n" to R.drawable.ic_snow_24_dark,
            "50n" to R.drawable.ic_clouds_24_dark
        )
    }


    fun convertToModel(hourlyWeatherEntity: HourlyWeatherEntity): HourlyWeatherModel {
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return HourlyWeatherModel(
            items = hourlyWeatherEntity.items.map { itemEntity ->
                HourlyWeatherItemModel(
                    now = itemEntity.now,
                    timeIndicator = itemEntity.timeIndicator,
                    timeString = simpleDateFormat.format(itemEntity.time),
                    iconId = itemEntity.iconId,
                    value = itemEntity.value
                )
            }
        )
    }

    fun convertToEntity(
        entityId: Int,
        latitude: Float,
        longitude: Float,
        response: HourlyWeatherResponse
    ): HourlyWeatherEntity {
        val appContext = OpenSourceWeatherApp.appContext
        return HourlyWeatherEntity(
            id = entityId,
            latitude = latitude,
            longitude = longitude,
            updateTime = Date(),
            items = response.hourly.mapIndexed { index, hourlyWeatherRs ->
                val now = index == 0
                val weather = hourlyWeatherRs.weather.first()
                val temperature = ceil(hourlyWeatherRs.temp).toInt().toString() + "°"
                val forecastDate = Date(hourlyWeatherRs.dt * 1000L)    //convert to milliseconds
                HourlyWeatherItemPojoEntity(
                    now = now,
                    timeIndicator = forecastDate.timeIndicator,
                    time = forecastDate,
                    iconId = if (now) ICONS_MAP[weather.icon]!! else ICONS_MAP_DARK[weather.icon]!!,
                    value = if (now) appContext.getString(R.string.now) else temperature
                )
            }
        )
    }
}