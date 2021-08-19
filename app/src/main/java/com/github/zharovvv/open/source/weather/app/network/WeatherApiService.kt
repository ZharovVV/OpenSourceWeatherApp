package com.github.zharovvv.open.source.weather.app.network

import com.github.zharovvv.open.source.weather.app.network.dto.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather?")
    fun getCurrentWeatherByCoordinates(
        @Query(value = "lat")
        lat: Float,
        @Query(value = "lon")
        lon: Float,
        @Query(value = "appid")
        apiKey: String
    ): Single<WeatherResponse>
}