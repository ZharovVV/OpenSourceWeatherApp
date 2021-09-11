package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.dto.WeatherResponse
import io.reactivex.Single

class WeatherRepository {

    private val weatherDtoToModelConverter = WeatherResponseToModelConverter()

    fun requestTodayWeather(lat: Float, lon: Float): Single<WeatherTodayModel> {
        return OpenSourceWeatherApp.weatherApiService.getCurrentWeatherByCoordinates(
            lat,
            lon,
            BuildConfig.WEATHER_API_KEY
        ).map { weatherResponse: WeatherResponse ->
            var model: WeatherTodayModel? = null
            try {
                model = weatherDtoToModelConverter.convert(weatherResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            model
        }
    }

}