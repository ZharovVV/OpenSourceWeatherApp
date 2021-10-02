package com.github.zharovvv.open.source.weather.app.repository

object HourlyWeatherRepositoryProvider {
    val hourlyWeatherRepository by lazy { HourlyWeatherRepository() }
}