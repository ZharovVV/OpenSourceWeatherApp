package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.github.zharovvv.open.source.weather.app.di.ApplicationComponent
import com.github.zharovvv.open.source.weather.app.di.DaggerApplicationComponent
import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import com.github.zharovvv.open.source.weather.app.models.domain.ThemeMode

class OpenSourceWeatherApp : Application() {

    companion object {
        val appContext get() = _appContext
        private lateinit var _appContext: Context
    }

    internal lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        appComponent = DaggerApplicationComponent.builder()
            .withAppContext(appContext = this)
            .build()
        _appContext = applicationContext
        setUpAppTheme()
        super.onCreate()
    }

    private fun setUpAppTheme() {
        val themePreferenceModel: PreferenceModel.ThemePreferenceModel =
            appComponent.preferencesRepository.requestThemePreference()
        AppCompatDelegate.setDefaultNightMode(
            when (themePreferenceModel.themeMode) {
                ThemeMode.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeMode.DEFAULT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}

val Context.appComponent: ApplicationComponent
    get() = when (this) {
        is OpenSourceWeatherApp -> appComponent
        else -> this.applicationContext.appComponent
    }