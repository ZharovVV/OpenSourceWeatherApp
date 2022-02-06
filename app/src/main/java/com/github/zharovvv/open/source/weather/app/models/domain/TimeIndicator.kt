package com.github.zharovvv.open.source.weather.app.models.domain

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R
import com.google.gson.annotations.SerializedName

enum class TimeIndicator(val description: String) {
    @SerializedName("TODAY")
    TODAY(OpenSourceWeatherApp.appContext.getString(R.string.today)),
    @SerializedName("TOMORROW")
    TOMORROW(OpenSourceWeatherApp.appContext.getString(R.string.tomorrow)),
    @SerializedName("DAY_AFTER_TOMORROW")
    DAY_AFTER_TOMORROW(OpenSourceWeatherApp.appContext.getString(R.string.day_after_tomorrow)),
    @SerializedName("FUTURE")
    FUTURE(OpenSourceWeatherApp.appContext.getString(R.string.empty))
}