package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object WeekWeatherRepositoryProvider {
    val weekWeatherRepository by lazy {
        WeekWeatherRepository(
            weekWeatherDao = OpenSourceWeatherApp.appDatabase.weekWeatherDao(),
            weatherApiMapper = OpenSourceWeatherApp.weatherApiService,
            weekWeatherConverter = WeekWeatherConverter()
        )
    }
}