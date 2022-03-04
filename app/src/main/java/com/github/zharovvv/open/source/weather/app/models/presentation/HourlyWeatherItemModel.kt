package com.github.zharovvv.open.source.weather.app.models.presentation

import androidx.annotation.DrawableRes
import com.github.zharovvv.open.source.weather.app.models.domain.TimeIndicator

data class HourlyWeatherItemModel(
    val now: Boolean,
    val timeIndicator: TimeIndicator,
    val timeString: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)