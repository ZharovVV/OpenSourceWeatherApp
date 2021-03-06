package com.github.zharovvv.open.source.weather.app.models.presentation

data class LocationModel(
    val latitude: Float,
    val longitude: Float,
    val cityName: String,
    val countryName: String,
    val isRealLocation: Boolean,
)