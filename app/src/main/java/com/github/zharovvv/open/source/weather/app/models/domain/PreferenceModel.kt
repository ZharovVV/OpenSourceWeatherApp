package com.github.zharovvv.open.source.weather.app.models.domain

sealed class PreferenceModel(
    val key: String,
    val name: String,
    val value: String,
) {
    class SimplePreferenceModel(key: String, name: String, value: String) :
        PreferenceModel(key, name, value)

    class ThemePreferenceModel(key: String, name: String, value: String, val themeMode: ThemeMode) :
        PreferenceModel(key, name, value)

    class AutoUpdatePreferenceModel(
        key: String,
        name: String,
        value: String,
        val repeatIntervalInHours: Long,
    ) : PreferenceModel(key, name, value)
}

enum class ThemeMode {
    DAY, NIGHT, DEFAULT
}
