package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class WeekWeatherResponse(
    val daily: List<WeekWeatherItem>
)

data class WeekWeatherItem(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Float,
    val temp: Temperature,
    @SerializedName(value = "feels_like")
    val feelsLike: FeelsLikeTemperature,
    val pressure: Int,
    val humidity: Int,
    @SerializedName(value = "dew_point")
    val dewPoint: Float,
    @SerializedName(value = "wind_speed")
    val windSpeed: Float,
    @SerializedName(value = "wind_deg")
    val windDeg: Int,
    @SerializedName(value = "wind_gust")
    val windGust: Float,
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Float,
    val uvi: Float
)

data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
)

data class FeelsLikeTemperature(
    val day: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
)
