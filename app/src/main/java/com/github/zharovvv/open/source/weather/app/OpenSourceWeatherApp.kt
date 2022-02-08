package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.github.zharovvv.open.source.weather.app.di.ApplicationComponent
import com.github.zharovvv.open.source.weather.app.di.DaggerApplicationComponent
import com.github.zharovvv.open.source.weather.app.presentation.settings.PreferencesKeyProvider

class OpenSourceWeatherApp : Application() {

    companion object {
        val appContext get() = _appContext
        private lateinit var _appContext: Context
        private const val LOG_TAG = "ApplicationLifecycle"
    }

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        Log.i(LOG_TAG, "OpenSourceWeatherApp#onCreate")
        appComponent = DaggerApplicationComponent.builder()
            .withAppContext(appContext = this)
            .build()
        _appContext = applicationContext
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        setUpDefaultPreferencesIfNotExists(sharedPreferences)
        setUpAppTheme(sharedPreferences)
        super.onCreate()
    }

    private fun setUpDefaultPreferencesIfNotExists(sharedPreferences: SharedPreferences) {
        val themeCodeArray = resources.getStringArray(R.array.preference_theme_value_entries)
        val defaultThemeCode = themeCodeArray[2]
        val themePreference =
            sharedPreferences.getString(PreferencesKeyProvider.preferenceThemeKey, null)
        if (themePreference == null) {
            sharedPreferences.edit()
                .putString(PreferencesKeyProvider.preferenceThemeKey, defaultThemeCode)
                .apply()
        }
        val autoUpdateCodeArray =
            resources.getStringArray(R.array.preference_auto_update_value_entries)
        val defaultAutoUpdateCode = autoUpdateCodeArray[0]
        val autoUpdatePreference =
            sharedPreferences.getString(PreferencesKeyProvider.preferenceAutoUpdateKey, null)
        if (autoUpdatePreference == null) {
            sharedPreferences.edit()
                .putString(PreferencesKeyProvider.preferenceAutoUpdateKey, defaultAutoUpdateCode)
                .apply()
        }
    }

    private fun setUpAppTheme(sharedPreferences: SharedPreferences) {
        val themeCodeArray = resources.getStringArray(R.array.preference_theme_value_entries)
        val dayThemeCode = themeCodeArray[0]
        val nightThemeCode = themeCodeArray[1]
        val defaultThemeCode = themeCodeArray[2]
        when (sharedPreferences.getString(
            PreferencesKeyProvider.preferenceThemeKey,
            defaultThemeCode
        )) {
            dayThemeCode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            nightThemeCode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}

val Context.appComponent: ApplicationComponent
    get() = when (this) {
        is OpenSourceWeatherApp -> appComponent
        else -> this.applicationContext.appComponent
    }