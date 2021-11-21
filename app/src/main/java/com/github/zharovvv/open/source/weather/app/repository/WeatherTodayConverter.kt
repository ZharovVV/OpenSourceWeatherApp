package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.database.entity.DetailedWeatherParamPojoEntity
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.model.DetailedWeatherParamModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.toTitleCase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WeatherTodayConverter :
    BaseConverter<CurrentWeatherResponse, WeatherTodayEntity, WeatherTodayModel> {

    companion object {
        private const val IC_WIND_SPEED = "ic_wind_speed"
        private const val IC_FEELS_LIKE = "ic_feels_like"
        private const val IC_HUMIDITY = "ic_humidity"
        private const val IC_PRESSURE = "ic_pressure"
        private val ICONS_MAP: Map<String, Int> = hashMapOf(
            "01d" to R.drawable.ic_clear_sky_60,
            "02d" to R.drawable.ic_few_clouds_60,
            "03d" to R.drawable.ic_clouds_60,
            "04d" to R.drawable.ic_clouds_60,
            "09d" to R.drawable.ic_rain_60,
            "10d" to R.drawable.ic_rain_60,
            "11d" to R.drawable.ic_thunderstorm_60,
            "13d" to R.drawable.ic_snow_60,
            "50d" to R.drawable.ic_clouds_60,

            "01n" to R.drawable.ic_clear_sky_60_night,
            "02n" to R.drawable.ic_few_clouds_60_night,
            "03n" to R.drawable.ic_clouds_60,
            "04n" to R.drawable.ic_clouds_60,
            "09n" to R.drawable.ic_rain_60,
            "10n" to R.drawable.ic_rain_60,
            "11n" to R.drawable.ic_thunderstorm_60,
            "13n" to R.drawable.ic_snow_60,
            "50n" to R.drawable.ic_clouds_60,

            IC_WIND_SPEED to R.drawable.ic_wind_speed_24,
            IC_FEELS_LIKE to R.drawable.ic_feels_like_24,
            IC_HUMIDITY to R.drawable.ic_humidity_24,
            IC_PRESSURE to R.drawable.ic_pressure_24
        )
        private val WEATHER_ID_DESCRIPTIONS: Map<Int, Int> = hashMapOf(
            200 to R.string.thunderstorm_with_rain,
            201 to R.string.thunderstorm_with_rain,
            202 to R.string.thunderstorm_with_rain,
            210 to R.string.thunderstorm,
            211 to R.string.thunderstorm,
            212 to R.string.thunderstorm,
            221 to R.string.thunderstorm,
            230 to R.string.thunderstorm,
            231 to R.string.thunderstorm,
            232 to R.string.thunderstorm,

            300 to R.string.drizzle,
            301 to R.string.drizzle,
            302 to R.string.drizzle,
            310 to R.string.drizzle_rain,
            311 to R.string.drizzle_rain,
            312 to R.string.drizzle_rain,
            313 to R.string.drizzle_rain,
            313 to R.string.drizzle_rain,
            321 to R.string.shower_drizzle,

            500 to R.string.rain,
            501 to R.string.rain,
            502 to R.string.heavy_rain,
            503 to R.string.heavy_rain,
            504 to R.string.heavy_rain,
            511 to R.string.freezing_rain,
            520 to R.string.heavy_rain,
            521 to R.string.heavy_rain,
            522 to R.string.heavy_rain,
            531 to R.string.heavy_rain,

            600 to R.string.snow,
            601 to R.string.snow,
            602 to R.string.heavy_snow,
            611 to R.string.sleet,
            612 to R.string.sleet,
            613 to R.string.sleet,
            615 to R.string.rain_snow,
            616 to R.string.rain_snow,
            620 to R.string.heavy_snow,
            621 to R.string.heavy_snow,
            622 to R.string.heavy_snow,

            701 to R.string.mist,
            711 to R.string.mist,
            721 to R.string.mist,
            731 to R.string.dust,
            741 to R.string.mist,
            751 to R.string.dust,
            761 to R.string.dust,
            762 to R.string.volcanic_ash,
            771 to R.string.squalls,
            781 to R.string.tornado,

            800 to R.string.clear_sky,
            801 to R.string.few_clouds,
            802 to R.string.clouds,
            803 to R.string.clouds,
            804 to R.string.clouds
        )
    }

    override fun convertToModel(entity: WeatherTodayEntity): WeatherTodayModel {
        val iconId: Int = ICONS_MAP[entity.iconId]!!
        return WeatherTodayModel(
            iconId = iconId,
            description = entity.description,
            dateString = entity.dateString,
            shortDateString = entity.shortDateString,
            temperature = entity.temperature,
            detailedWeatherParams = entity.detailedWeatherParams.map { weatherParamPojoEntity ->
                DetailedWeatherParamModel(
                    name = weatherParamPojoEntity.name,
                    iconId = ICONS_MAP[weatherParamPojoEntity.iconId]!!,
                    value = weatherParamPojoEntity.value
                )
            },
            locationModel = LocationModel(
                latitude = entity.latitude,
                longitude = entity.longitude,
                cityName = entity.cityName,
                countryName = entity.countryName
            )
        )
    }

    override fun convertToEntity(
        entityId: Int,
        locationModel: LocationModel,
        response: CurrentWeatherResponse
    ): WeatherTodayEntity {
        val updateTime = Date()
        val weather = response.weather.first()
        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
        val shortDateFormat = SimpleDateFormat("dd.MM, HH:mm", Locale.getDefault())
        val appContext = OpenSourceWeatherApp.appContext
        return WeatherTodayEntity(
            id = entityId,
            latitude = locationModel.latitude,
            longitude = locationModel.longitude,
            cityName = locationModel.cityName,
            countryName = locationModel.countryName,
            updateTime = updateTime,
            iconId = weather.icon,
            description = appContext.getString(WEATHER_ID_DESCRIPTIONS[weather.id]!!),
            dateString = simpleDateFormat.format(updateTime).toTitleCase(),
            shortDateString = shortDateFormat.format(updateTime),
            temperature = ceil(response.mainInfo.temp).toInt().toString() + "°",
            detailedWeatherParams = listOf(
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.wind),
                    iconId = IC_WIND_SPEED,
                    value = response.wind.speed.toString() + " м/с"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.feels_like),
                    iconId = IC_FEELS_LIKE,
                    value = ceil(response.mainInfo.feelsLike).toInt().toString() + "°"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.humidity),
                    iconId = IC_HUMIDITY,
                    value = response.mainInfo.humidity.toString() + " %"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.pressure),
                    iconId = IC_PRESSURE,
                    value = response.mainInfo.pressure.toString() + " кПа"
                )
            )
        )
    }
}