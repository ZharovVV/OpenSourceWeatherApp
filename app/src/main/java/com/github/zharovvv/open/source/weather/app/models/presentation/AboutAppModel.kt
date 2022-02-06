package com.github.zharovvv.open.source.weather.app.models.presentation

data class AboutAppModel(val parameters: List<AboutAppParameter>)

data class AboutAppParameter(val parameterName: String, val parameterValue: CharSequence)