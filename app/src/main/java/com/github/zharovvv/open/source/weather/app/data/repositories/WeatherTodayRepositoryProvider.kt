package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object WeatherTodayRepositoryProvider {
    val weatherRepository by lazy {
        WeatherTodayRepository(
            weatherTodayDao = OpenSourceWeatherApp.appDatabase.weatherTodayDao(),
            weatherApiMapper = OpenSourceWeatherApp.weatherApiService,
            weatherTodayConverter = WeatherTodayConverter()
        )
    }
}