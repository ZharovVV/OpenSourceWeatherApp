package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class HourlyWeatherResponse(
    @SerializedName(value = "hourly")
    val hourly: List<HourlyWeatherItem>
)

data class HourlyWeatherItem(
    @SerializedName(value = "dt")
    val dt: Long,
    @SerializedName(value = "temp")
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    @SerializedName(value = "pressure")
    val pressure: Int,
    @SerializedName(value = "humidity")
    val humidity: Int,
    @SerializedName(value = "dew_point")
    val dewPoint: Float,
    @SerializedName(value = "uvi")
    val uvi: Float,
    @SerializedName(value = "clouds")
    val clouds: Int,
    @SerializedName(value = "visibility")
    val visibility: Int,
    @SerializedName(value = "wind_speed")
    val windSpeed: Float,
    @SerializedName(value = "wind_deg")
    val windDeg: Int,
    @SerializedName(value = "wind_gust")
    val windGust: Float,
    @SerializedName(value = "weather")
    val weather: List<Weather>,
    @SerializedName(value = "pop")
    val pop: Float
)

