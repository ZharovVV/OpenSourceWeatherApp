package com.github.zharovvv.open.source.weather.app.data.remote

import com.github.zharovvv.open.source.weather.app.BuildConfig
import io.reactivex.Single

class WeatherApiKeyProviderDebugImpl : WeatherApiKeyProvider {

    override fun requestWeatherApiKey(): Single<String> {
        return Single.just(BuildConfig.WEATHER_API_KEY)
    }

    override fun requestWeatherApiKeySync(): String {
        return BuildConfig.WEATHER_API_KEY
    }
}