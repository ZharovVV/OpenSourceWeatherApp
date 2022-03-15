package com.github.zharovvv.open.source.weather.app.data.remote

import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Single

class WeatherApiKeyProviderProdImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : WeatherApiKeyProvider {

    override fun requestWeatherApiKey(): Single<String> {
        val apiToken: String = firebaseRemoteConfig.getString(OPENWEATHERMAP_API_KEY_CONFIG_KEY)
        return if (apiToken.isEmpty()) {
            Single.create { emitter ->
                firebaseRemoteConfig.fetchAndActivate()
                    .addOnSuccessListener {
                        emitter.onSuccess(
                            firebaseRemoteConfig.getString(OPENWEATHERMAP_API_KEY_CONFIG_KEY)
                        )
                    }
                    .addOnFailureListener { emitter.tryOnError(it) }
            }
        } else {
            Single.just(apiToken)
        }
    }

    override fun requestWeatherApiKeySync(): String {
        val apiToken: String = firebaseRemoteConfig.getString(OPENWEATHERMAP_API_KEY_CONFIG_KEY)
        if (apiToken.isNotEmpty()) {
            return apiToken
        }
        Tasks.await(firebaseRemoteConfig.fetchAndActivate())
        return firebaseRemoteConfig.getString(OPENWEATHERMAP_API_KEY_CONFIG_KEY)
    }

    companion object {
        private const val OPENWEATHERMAP_API_KEY_CONFIG_KEY = "openweathermap_api_key"
    }
}