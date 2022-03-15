package com.github.zharovvv.open.source.weather.app.data.remote

import io.reactivex.Single

interface WeatherApiKeyProvider {

    fun requestWeatherApiKey(): Single<String>

    fun requestWeatherApiKeySync(): String
}