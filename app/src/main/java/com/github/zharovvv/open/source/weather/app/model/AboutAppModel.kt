package com.github.zharovvv.open.source.weather.app.model

data class AboutAppModel(val parameters: List<AboutAppParameter>)

data class AboutAppParameter(val parameterName: String, val parameterValue: CharSequence)