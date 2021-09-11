package com.github.zharovvv.open.source.weather.app.ui.weather.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class WeatherTodayViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()
    private val _weatherTodayData = MutableLiveData<WeatherTodayModel>()
    val weatherTodayData: LiveData<WeatherTodayModel> get() = _weatherTodayData

    private val compositeDisposable = CompositeDisposable()

    fun updateWeatherToday(lat: Float, lon: Float) {
        compositeDisposable += weatherRepository.requestTodayWeather(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { weatherTodayModel: WeatherTodayModel ->
                    _weatherTodayData.value = weatherTodayModel
                }, {

                }
            )


    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}