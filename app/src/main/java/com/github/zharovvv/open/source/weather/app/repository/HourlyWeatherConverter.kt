package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherItemPojoEntity
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherItemModel
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.network.dto.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.timeIndicator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class HourlyWeatherConverter :
    BaseConverter<HourlyWeatherResponse, HourlyWeatherEntity, HourlyWeatherModel> {

    companion object {
        //TODO Вынести получение иконок в другое место
        val ICONS_MAP: Map<String, Int> = hashMapOf(
            "01d" to R.drawable.ic_clear_sky_24,
            "02d" to R.drawable.ic_few_clouds_24,
            "03d" to R.drawable.ic_clouds_24,
            "04d" to R.drawable.ic_clouds_24,
            "09d" to R.drawable.ic_rain_24,
            "10d" to R.drawable.ic_rain_24,
            "11d" to R.drawable.ic_thunderstorm_24,
            "13d" to R.drawable.ic_snow_24,
            "50d" to R.drawable.ic_clouds_24,

            "01n" to R.drawable.ic_clear_sky_24_night,
            "02n" to R.drawable.ic_few_clouds_24_night,
            "03n" to R.drawable.ic_clouds_24,
            "04n" to R.drawable.ic_clouds_24,
            "09n" to R.drawable.ic_rain_24,
            "10n" to R.drawable.ic_rain_24,
            "11n" to R.drawable.ic_thunderstorm_24,
            "13n" to R.drawable.ic_snow_24,
            "50n" to R.drawable.ic_clouds_24
        )
        val ICONS_MAP_DARK: Map<String, Int> = hashMapOf(
            "01d" to R.drawable.ic_clear_sky_24,
            "02d" to R.drawable.ic_few_clouds_24_dark,
            "03d" to R.drawable.ic_clouds_24_dark,
            "04d" to R.drawable.ic_clouds_24_dark,
            "09d" to R.drawable.ic_rain_24_dark,
            "10d" to R.drawable.ic_rain_24_dark,
            "11d" to R.drawable.ic_thunderstorm_24_dark,
            "13d" to R.drawable.ic_snow_24_dark,
            "50d" to R.drawable.ic_clouds_24_dark,

            "01n" to R.drawable.ic_clear_sky_24_night,
            "02n" to R.drawable.ic_few_clouds_24_dark_night,
            "03n" to R.drawable.ic_clouds_24_dark,
            "04n" to R.drawable.ic_clouds_24_dark,
            "09n" to R.drawable.ic_rain_24_dark,
            "10n" to R.drawable.ic_rain_24_dark,
            "11n" to R.drawable.ic_thunderstorm_24_dark,
            "13n" to R.drawable.ic_snow_24_dark,
            "50n" to R.drawable.ic_clouds_24_dark
        )
    }


    override fun convertToModel(entity: HourlyWeatherEntity): HourlyWeatherModel {
        val appContext = OpenSourceWeatherApp.appContext
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return HourlyWeatherModel(
            items = entity.items.map { itemEntity ->
                val now = itemEntity.now
                HourlyWeatherItemModel(
                    now = now,
                    timeIndicator = itemEntity.timeIndicator,
                    timeString = simpleDateFormat.format(itemEntity.time),
                    iconId = if (now) ICONS_MAP[itemEntity.iconId]!! else ICONS_MAP_DARK[itemEntity.iconId]!!,
                    value = if (now) appContext.getString(R.string.now) else itemEntity.value
                )
            }
        )
    }

    override fun convertToEntity(
        entityId: Int,
        locationModel: LocationModel,
        response: HourlyWeatherResponse
    ): HourlyWeatherEntity {
        return HourlyWeatherEntity(
            id = entityId,
            latitude = locationModel.latitude,
            longitude = locationModel.longitude,
            updateTime = Date(),
            items = response.hourly.mapIndexed { index, hourlyWeatherItemRs ->
                val now = index == 0
                val weather = hourlyWeatherItemRs.weather.first()
                val temperature = ceil(hourlyWeatherItemRs.temp).toInt().toString() + "°"
                val forecastDate = Date(hourlyWeatherItemRs.dt * 1000L)    //convert to milliseconds
                HourlyWeatherItemPojoEntity(
                    now = now,
                    timeIndicator = forecastDate.timeIndicator,
                    time = forecastDate,
                    iconId = weather.icon,
                    value = temperature
                )
            }
        )
    }
}