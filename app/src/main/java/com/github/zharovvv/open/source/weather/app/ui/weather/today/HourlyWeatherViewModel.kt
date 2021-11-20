package com.github.zharovvv.open.source.weather.app.ui.weather.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.repository.HourlyWeatherRepositoryProvider
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class HourlyWeatherViewModel : ViewModel() {

    private val locationRepository = LocationRepositoryProvider.locationRepository
    private val hourlyWeatherRepository = HourlyWeatherRepositoryProvider.hourlyWeatherRepository
    private val _hourlyWeatherData = MutableLiveData<DataState<HourlyWeatherModel>>()
    val hourlyWeatherData: LiveData<DataState<HourlyWeatherModel>> get() = _hourlyWeatherData

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(Schedulers.io())
            .subscribe {
                hourlyWeatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = false
                )
            }
        compositeDisposable += hourlyWeatherRepository.observableData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _hourlyWeatherData.value = it }
    }

    fun requestHourlyWeather() {
        compositeDisposable += Single.fromCallable {
            locationRepository.getLastKnownLocation()
        }
            .filter { locationModel: LocationModel? -> locationModel != null }
            .map {
                hourlyWeatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = true
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Functions.emptyConsumer(), { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}