package com.github.zharovvv.open.source.weather.app.ui.weather.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import com.github.zharovvv.open.source.weather.app.repository.WeatherTodayRepositoryProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class WeatherTodayViewModel : ViewModel() {

    private val locationRepository = LocationRepositoryProvider.locationRepository
    private val weatherRepository = WeatherTodayRepositoryProvider.weatherRepository
    private val _weatherTodayData = MutableLiveData<WeatherTodayModel>()
    val weatherTodayData: LiveData<WeatherTodayModel> get() = _weatherTodayData

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(Schedulers.io())
            .subscribe {
                weatherRepository.requestTodayWeather(it.latitude, it.longitude)
            }
        compositeDisposable += weatherRepository.weatherTodayObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _weatherTodayData.value = it }
    }

    fun requestWeatherToday(lat: Float, lon: Float) {
        compositeDisposable += Single.fromCallable {
            weatherRepository.requestTodayWeather(lat, lon)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}