package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class HourlyWeatherResponse(
    val hourly: List<HourlyWeatherItem>
)

data class HourlyWeatherItem(
    val dt: Long,
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    @SerializedName(value = "dew_point")
    val dewPoint: Float,
    val uvi: Float,
    val clouds: Int,
    val visibility: Int,
    @SerializedName(value = "wind_speed")
    val windSpeed: Float,
    @SerializedName(value = "wind_deg")
    val windDeg: Int,
    @SerializedName(value = "wind_gust")
    val windGust: Float,
    val weather: List<Weather>,
    val pop: Float
)

