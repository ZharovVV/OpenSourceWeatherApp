package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.zharovvv.open.source.weather.app.database.AppDatabase
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        WEATHER_API_SERVICE = retrofit.create(WeatherApiService::class.java)
        _appContext = applicationContext
        APP_DATABASE = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "location_database"
        ).build()
    }
}