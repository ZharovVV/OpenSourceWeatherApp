package com.github.zharovvv.open.source.weather.app.data.repositories

/**
 * Удалить при переходе на какой-нибудь фреймворк DI
 */
object LocationRepositoryProvider {
    val locationRepository = LocationRepository()
}