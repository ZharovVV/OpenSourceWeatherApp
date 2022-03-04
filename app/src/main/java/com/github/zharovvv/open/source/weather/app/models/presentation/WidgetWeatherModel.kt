package com.github.zharovvv.open.source.weather.app.models.presentation

import androidx.annotation.DrawableRes

/**
 * Модель виджета погоды
 */
data class WidgetWeatherModel(
    @DrawableRes
    val weatherIconId: Int,
    val temperature: String,
    val locationDescription: String,
    val forecastDateString: String
)