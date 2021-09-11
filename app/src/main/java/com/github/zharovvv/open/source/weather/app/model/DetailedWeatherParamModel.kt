package com.github.zharovvv.open.source.weather.app.model

import androidx.annotation.DrawableRes

data class DetailedWeatherParamModel(
    val name: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)
