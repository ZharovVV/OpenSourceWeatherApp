package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object HourlyWeatherRepositoryProvider {
    val hourlyWeatherRepository by lazy {
        HourlyWeatherRepository(
            hourlyWeatherDao = OpenSourceWeatherApp.appDatabase.hourlyWeatherDao(),
            weatherApiService = OpenSourceWeatherApp.weatherApiService,
            hourlyWeatherConverter = HourlyWeatherConverter()
        )
    }
}