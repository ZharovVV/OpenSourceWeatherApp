package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object WeatherTodayRepositoryProvider {
    val weatherRepository by lazy {
        WeatherTodayRepository(
            weatherTodayDao = OpenSourceWeatherApp.appDatabase.weatherTodayDao(),
            weatherApiService = OpenSourceWeatherApp.weatherApiService,
            weatherTodayConverter = WeatherTodayConverter()
        )
    }
}