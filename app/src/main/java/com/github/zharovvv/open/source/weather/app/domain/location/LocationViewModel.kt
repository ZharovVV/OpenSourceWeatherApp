package com.github.zharovvv.open.source.weather.app.domain.location

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.ILocationRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.util.startService
import com.github.zharovvv.open.source.weather.app.util.stopService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LocationViewModel(
    //TODO инстанс будет не нужен, если убрать "ненужный" сервис для прослушки обновления геолокации
    private val application: Application,
    private val locationRepository: ILocationRepository
) : ViewModel() {

    private val _locationLiveData = MutableLiveData<LocationModel>()
    val locationData: LiveData<LocationModel> get() = _locationLiveData

    private val compositeDisposable = CompositeDisposable()
    private var locationServiceStarted: Boolean = false

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .subscribe(
                { locationModel: LocationModel ->
                    _locationLiveData.value = locationModel
                },
                { throwable: Throwable ->
                    throwable.printStackTrace()
                }
            )
    }

    fun requestLocation() {
        if (!locationServiceStarted) {
            application.startService<LocationService>()
            locationServiceStarted = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        application.stopService<LocationService>()
        compositeDisposable.clear()
    }
}