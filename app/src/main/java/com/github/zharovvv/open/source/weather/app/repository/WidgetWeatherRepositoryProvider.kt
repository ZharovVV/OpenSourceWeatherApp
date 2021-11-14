package com.github.zharovvv.open.source.weather.app.repository

object WidgetWeatherRepositoryProvider {
    val widgetWeatherRepository = WidgetWeatherRepository(
        locationRepository = LocationRepositoryProvider.locationRepository,
        weatherTodayRepository = WeatherTodayRepositoryProvider.weatherRepository
    )
}