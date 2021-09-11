package com.github.zharovvv.open.source.weather.app

import android.app.Application
import android.content.Context
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class OpenSourceWeatherApp : Application() {

    companion object {
        lateinit var weatherApiService: WeatherApiService
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        weatherApiService = retrofit.create(WeatherApiService::class.java)
        appContext = applicationContext
    }
}