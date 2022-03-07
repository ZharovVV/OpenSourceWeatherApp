package com.github.zharovvv.open.source.weather.app.presentation.week.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.IWeekWeatherRepository
import com.github.zharovvv.open.source.weather.app.domain.location.ILocationRepository
import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeekWeatherModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class WeekWeatherViewModel(
    private val locationRepository: ILocationRepository,
    private val weekWeatherRepository: IWeekWeatherRepository
) : ViewModel() {

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