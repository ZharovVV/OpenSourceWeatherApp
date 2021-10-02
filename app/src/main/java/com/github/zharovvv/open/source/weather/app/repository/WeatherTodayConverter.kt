package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.database.entity.DetailedWeatherParamPojoEntity
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.model.DetailedWeatherParamModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WeatherTodayConverter {

    companion object {
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

            "01n" to R.drawable.ic_clear_sky_60,
            "02n" to R.drawable.ic_few_clouds_60,
            "03n" to R.drawable.ic_clouds_60,
            "04n" to R.drawable.ic_clouds_60,
            "09n" to R.drawable.ic_rain_60,
            "10n" to R.drawable.ic_rain_60,
            "11n" to R.drawable.ic_thunderstorm_60,
            "13n" to R.drawable.ic_snow_60,
            "50n" to R.drawable.ic_clouds_60
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

    fun convertToModel(weatherTodayEntity: WeatherTodayEntity): WeatherTodayModel {
        return WeatherTodayModel(
            iconId = weatherTodayEntity.iconId,
            description = weatherTodayEntity.description,
            dateString = weatherTodayEntity.dateString,
            temperature = weatherTodayEntity.temperature,
            detailedWeatherParams = weatherTodayEntity.detailedWeatherParams.map { weatherParamPojoEntity ->
                DetailedWeatherParamModel(
                    name = weatherParamPojoEntity.name,
                    iconId = weatherParamPojoEntity.iconId,
                    value = weatherParamPojoEntity.value
                )
            }
        )
    }

    fun convertToEntity(
        entityId: Int,
        latitude: Float,
        longitude: Float,
        response: CurrentWeatherResponse,
    ): WeatherTodayEntity {
        val weather = response.weather.first()
        val iconId: Int = ICONS_MAP[weather.icon]!!
        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
        val appContext = OpenSourceWeatherApp.appContext
        return WeatherTodayEntity(
            id = entityId,
            latitude = latitude,
            longitude = longitude,
            updateTime = Date(),
            iconId = iconId,
            description = appContext.getString(WEATHER_ID_DESCRIPTIONS[weather.id]!!),
            dateString = simpleDateFormat.format(Date()).capitalize(Locale.getDefault()),
            temperature = ceil(response.mainInfo.temp).toInt().toString() + "°",
            detailedWeatherParams = listOf(
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.wind),
                    iconId = R.drawable.ic_wind_speed_24,
                    value = response.wind.speed.toString() + " м/с"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.feels_like),
                    iconId = R.drawable.ic_feels_like_24,
                    value = ceil(response.mainInfo.feelsLike).toInt().toString() + "°"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.humidity),
                    iconId = R.drawable.ic_humidity_24,
                    value = response.mainInfo.humidity.toString() + " %"
                ),
                DetailedWeatherParamPojoEntity(
                    name = appContext.getString(R.string.pressure),
                    iconId = R.drawable.ic_pressure_24,
                    value = response.mainInfo.pressure.toString() + " кПа"
                )
            )
        )
    }
}