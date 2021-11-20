package com.github.zharovvv.open.source.weather.app.ui.weather.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeekWeatherModel
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import com.github.zharovvv.open.source.weather.app.repository.WeekWeatherRepositoryProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class WeekWeatherViewModel : ViewModel() {

    private val locationRepository = LocationRepositoryProvider.locationRepository
    private val weekWeatherRepository = WeekWeatherRepositoryProvider.weekWeatherRepository
    private val _weatherTodayData = MutableLiveData<DataState<WeekWeatherModel>>()
    val weatherTodayData: LiveData<DataState<WeekWeatherModel>> get() = _weatherTodayData

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(Schedulers.io())
            .subscribe {
                weekWeatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = false
                )
            }
        compositeDisposable += weekWeatherRepository.observableData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _weatherTodayData.value = it }
    }

    fun requestWeatherToday() {
        compositeDisposable += Single.fromCallable {
            locationRepository.getLastKnownLocation()
        }
            .filter { locationModel: LocationModel? -> locationModel != null }
            .map {
                weekWeatherRepository.requestData(
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