package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class WeekWeatherResponse(
    @SerializedName(value = "daily")
    val daily: List<WeekWeatherItem>
)

data class WeekWeatherItem(
    @SerializedName(value = "dt")
    val dt: Long,
    @SerializedName(value = "sunrise")
    val sunrise: Long,
    @SerializedName(value = "sunset")
    val sunset: Long,
    @SerializedName(value = "moonrise")
    val moonrise: Long,
    @SerializedName(value = "moonset")
    val moonset: Long,
    @SerializedName(value = "moon_phase")
    val moonPhase: Float,
    @SerializedName(value = "temp")
    val temp: Temperature,
    @SerializedName(value = "feels_like")
    val feelsLike: FeelsLikeTemperature,
    @SerializedName(value = "pressure")
    val pressure: Int,
    @SerializedName(value = "humidity")
    val humidity: Int,
    @SerializedName(value = "dew_point")
    val dewPoint: Float,
    @SerializedName(value = "wind_speed")
    val windSpeed: Float,
    @SerializedName(value = "wind_deg")
    val windDeg: Int,
    @SerializedName(value = "wind_gust")
    val windGust: Float,
    @SerializedName(value = "weather")
    val weather: List<Weather>,
    @SerializedName(value = "clouds")
    val clouds: Int,
    @SerializedName(value = "pop")
    val pop: Float,
    @SerializedName(value = "uvi")
    val uvi: Float
)

data class Temperature(
    @SerializedName(value = "day")
    val day: Float,
    @SerializedName(value = "min")
    val min: Float,
    @SerializedName(value = "max")
    val max: Float,
    @SerializedName(value = "night")
    val night: Float,
    @SerializedName(value = "eve")
    val eve: Float,
    @SerializedName(value = "morn")
    val morn: Float
)

data class FeelsLikeTemperature(
    @SerializedName(value = "day")
    val day: Float,
    @SerializedName(value = "night")
    val night: Float,
    @SerializedName(value = "eve")
    val eve: Float,
    @SerializedName(value = "morn")
    val morn: Float
)
