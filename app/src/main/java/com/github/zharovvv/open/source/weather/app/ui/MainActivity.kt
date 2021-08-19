package com.github.zharovvv.open.source.weather.app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.databinding.ActivityMainBinding
import com.github.zharovvv.open.source.weather.app.network.dto.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            textViewTestWeather.text = "Погода"
            buttonStart.setOnClickListener {
                makeRequest()
            }
        }
    }

    private fun makeRequest() {
        val apiKey = BuildConfig.WEATHER_API_KEY
        compositeDisposable +=
            OpenSourceWeatherApp.weatherApiService.getCurrentWeatherByCoordinates(
                56.833332f,
                60.583332f,
                apiKey
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { weatherResponse: WeatherResponse ->
                        Log.i("Retrofit", weatherResponse.toString())
                        val currentTemperature = weatherResponse.mainInfo.temp
                        with(binding) {
                            textViewTestWeather.text = currentTemperature.toString()
                        }
                    },
                    { throwable: Throwable ->
                        Log.e("Retrofit", throwable.message ?: "empty message")
                        throwable.message?.let {
                            binding.textViewTestWeather.text = it
                        }
                    }
                )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}