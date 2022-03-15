package com.github.zharovvv.open.source.weather.app.data.remote

import com.github.zharovvv.open.source.weather.app.models.data.remote.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.models.data.remote.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.models.data.remote.WeekWeatherResponse
import retrofit2.Call

class WeatherApiGateway(
    private val weatherApiMapper: WeatherApiMapper,
    private val weatherApiKeyProvider: WeatherApiKeyProvider,
) {

    fun getCurrentWeatherByCoordinates(
        latitude: Float,
        longitude: Float,
    ): Call<CurrentWeatherResponse> {
        return weatherApiMapper.getCurrentWeatherByCoordinates(
            lat = latitude,
            lon = longitude,
            apiKey = weatherApiKeyProvider.requestWeatherApiKeySync()
        )
    }

    fun getHourlyWeatherByCoordinates(
        latitude: Float,
        longitude: Float,
    ): Call<HourlyWeatherResponse> {
        return weatherApiMapper.getHourlyWeatherByCoordinates(
            lat = latitude,
            lon = longitude,
            apiKey = weatherApiKeyProvider.requestWeatherApiKeySync()
        )
    }

    fun getWeekWeatherByCoordinates(
        latitude: Float,
        longitude: Float,
    ): Call<WeekWeatherResponse> {
        return weatherApiMapper.getWeekWeatherByCoordinates(
            lat = latitude,
            lon = longitude,
            apiKey = weatherApiKeyProvider.requestWeatherApiKeySync()
        )
    }
}