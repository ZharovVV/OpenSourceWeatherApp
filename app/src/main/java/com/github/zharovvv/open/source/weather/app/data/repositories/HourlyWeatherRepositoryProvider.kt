package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object HourlyWeatherRepositoryProvider {
    val hourlyWeatherRepository by lazy {
        HourlyWeatherRepository(
            hourlyWeatherDao = OpenSourceWeatherApp.appDatabase.hourlyWeatherDao(),
            weatherApiMapper = OpenSourceWeatherApp.weatherApiService,
            hourlyWeatherConverter = HourlyWeatherConverter()
        )
    }
}