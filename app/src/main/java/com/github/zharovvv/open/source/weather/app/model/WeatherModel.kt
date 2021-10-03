package com.github.zharovvv.open.source.weather.app.model

import androidx.annotation.DrawableRes
import com.github.zharovvv.open.source.weather.app.util.TimeIndicator

data class WeatherTodayModel(
    @DrawableRes
    val iconId: Int,
    val description: String,
    val dateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamModel>
)

data class DetailedWeatherParamModel(
    val name: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)

data class HourlyWeatherModel(
    val items: List<HourlyWeatherItemModel>
)

data class HourlyWeatherItemModel(
    val now: Boolean,
    val timeIndicator: TimeIndicator,
    val timeString: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)
