package com.github.zharovvv.open.source.weather.app.network

import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.network.dto.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.network.dto.WeekWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather?units=metric")
    fun getCurrentWeatherByCoordinates(
        @Query(value = "lat")
        lat: Float,
        @Query(value = "lon")
        lon: Float,
        @Query(value = "appid")
        apiKey: String
    ): Call<CurrentWeatherResponse>

    @GET("https://api.openweathermap.org/data/2.5/onecall?units=metric")
    fun getHourlyWeatherByCoordinates(
        @Query(value = "lat")
        lat: Float,
        @Query(value = "lon")
        lon: Float,
        @Query(value = "appid")
        apiKey: String
    ): Call<HourlyWeatherResponse>

    @GET("https://api.openweathermap.org/data/2.5/onecall?units=metric")
    fun getWeekWeatherByCoordinates(
        @Query(value = "lat")
        lat: Float,
        @Query(value = "lon")
        lon: Float,
        @Query(value = "appid")
        apiKey: String
    ): Call<WeekWeatherResponse>
}