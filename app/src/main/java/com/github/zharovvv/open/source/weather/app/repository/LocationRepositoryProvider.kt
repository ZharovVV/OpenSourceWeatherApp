package com.github.zharovvv.open.source.weather.app.repository

/**
 * Удалить при переходе на какой-нибудь фреймворк DI
 */
object LocationRepositoryProvider {
    val locationRepository = LocationRepository()
}