package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.zharovvv.open.source.weather.app.database.AppDatabase
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
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
    }

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        _appContext = applicationContext
        APP_DATABASE = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather_app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
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