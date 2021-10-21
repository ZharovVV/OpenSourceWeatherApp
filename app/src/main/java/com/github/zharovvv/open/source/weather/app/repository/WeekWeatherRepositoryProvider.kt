package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

object WeekWeatherRepositoryProvider {
    val weekWeatherRepository by lazy {
        WeekWeatherRepository(
            weekWeatherDao = OpenSourceWeatherApp.appDatabase.weekWeatherDao(),
            weatherApiService = OpenSourceWeatherApp.weatherApiService,
            weekWeatherConverter = WeekWeatherConverter()
        )
    }
}