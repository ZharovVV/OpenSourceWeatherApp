package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.github.zharovvv.open.source.weather.app.database.AppDatabase
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.ui.settings.PreferencesKeyProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenSourceWeatherApp : Application() {

    companion object {
        val weatherApiService get() = WEATHER_API_SERVICE
        val appContext get() = _appContext
        val appDatabase get() = APP_DATABASE
        private lateinit var WEATHER_API_SERVICE: WeatherApiService
        private lateinit var _appContext: Context
        private lateinit var APP_DATABASE: AppDatabase
        private const val LOG_TAG = "ApplicationLifecycle"
    }

    override fun onCreate() {
        Log.i(LOG_TAG, "OpenSourceWeatherApp#onCreate")
        _appContext = applicationContext
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        setUpDefaultPreferencesIfNotExists(sharedPreferences)
        setUpAppTheme(sharedPreferences)
        super.onCreate()
        configureRetrofit()
        APP_DATABASE = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather_app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
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
        val autoUpdateCodeArray = resources.getStringArray(R.array.preference_auto_update_value_entries)
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

    private fun configureRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        WEATHER_API_SERVICE = retrofit.create(WeatherApiService::class.java)
    }
}