package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp

/**
 * Удалить при переходе на какой-нибудь фреймворк DI
 */
object LocationRepositoryProvider {
    val locationRepository = LocationRepository(OpenSourceWeatherApp.appDatabase.locationDao())
}