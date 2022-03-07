package com.github.zharovvv.open.source.weather.app.domain.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LocationViewModel(
    private val locationRepository: ILocationRepository,
    //TODO тест (удалить!!!)
    private val preferencesRepository: IPreferencesRepository,
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
                    throwable.printStackTrace()
                }
            )
    }

    fun requestLocation() {
        //TODO тест (удалить!!!)
        preferencesRepository.updateSimplePreference("location_auto_update_key", "true")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}