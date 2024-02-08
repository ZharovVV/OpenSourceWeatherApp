package com.github.zharovvv.open.source.weather.app.domain.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.logger.Logger
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LocationViewModel(
    locationRepository: ILocationRepository,
) : ViewModel() {

    private val _locationLiveData = MutableLiveData<LocationModel>()
    val locationData: LiveData<LocationModel> get() = _locationLiveData

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { locationModel: LocationModel ->
                    _locationLiveData.value = locationModel
                },
                { throwable: Throwable ->
                    Logger.e("OpenSourceWeatherApp", "Ошибка при получении геолокации", throwable)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}