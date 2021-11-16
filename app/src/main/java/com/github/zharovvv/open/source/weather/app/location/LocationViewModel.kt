package com.github.zharovvv.open.source.weather.app.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.repository.LocationRepository
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import com.github.zharovvv.open.source.weather.app.util.startService
import com.github.zharovvv.open.source.weather.app.util.stopService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository: LocationRepository =
        LocationRepositoryProvider.locationRepository

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
            getApplication<OpenSourceWeatherApp>().startService<LocationService>()
            locationServiceStarted = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<OpenSourceWeatherApp>().stopService<LocationService>()
        compositeDisposable.clear()
    }
}