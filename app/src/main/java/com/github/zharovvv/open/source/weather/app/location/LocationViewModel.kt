package com.github.zharovvv.open.source.weather.app.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.zharovvv.open.source.weather.app.model.LocationModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val _locationLiveData = LocationLiveData(context = application)
    val locationData: LiveData<LocationModel> get() = _locationLiveData

    fun requestLocation() {
        _locationLiveData.requestLocation()
    }

    override fun onCleared() {
        super.onCleared()
        _locationLiveData.clear()
    }
}