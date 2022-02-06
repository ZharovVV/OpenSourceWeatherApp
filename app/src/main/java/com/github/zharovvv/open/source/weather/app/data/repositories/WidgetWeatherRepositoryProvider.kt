package com.github.zharovvv.open.source.weather.app.data.repositories

object WidgetWeatherRepositoryProvider {
    val widgetWeatherRepository = WidgetWeatherRepository(
        locationRepository = LocationRepositoryProvider.locationRepository,
        weatherTodayRepository = WeatherTodayRepositoryProvider.weatherRepository
    )
}