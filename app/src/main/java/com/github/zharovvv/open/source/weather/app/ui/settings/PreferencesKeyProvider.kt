package com.github.zharovvv.open.source.weather.app.ui.settings

import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R

object PreferencesKeyProvider {

    val preferenceThemeKey: String =
        OpenSourceWeatherApp.appContext.getString(R.string.preference_theme_key)

    val preferenceAutoUpdateKey: String =
        OpenSourceWeatherApp.appContext.getString(R.string.preference_auto_update_key)

}
