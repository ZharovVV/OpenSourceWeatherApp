package com.github.zharovvv.open.source.weather.app.repository

object WeatherTodayRepositoryProvider {
    val weatherRepository by lazy { WeatherTodayRepository() }
}