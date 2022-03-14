package com.github.zharovvv.open.source.weather.app.presentation.select.location.mode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.location.SelectLocationModeInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class SelectLocationModeViewModel(
    private val selectLocationModeInteractor: SelectLocationModeInteractor,
) : ViewModel() {

    private val _locationModeSelected = MutableLiveData<Unit>()
    val locationModeSelected: LiveData<Unit> get() = _locationModeSelected
    private val compositeDisposable = CompositeDisposable()

    init {
        selectLocationModeInteractor.autoUpdateLocationPreferencesChangesObservable()
            .subscribe { _locationModeSelected.value = Unit }
            .addTo(compositeDisposable)
    }

    fun enableAutoUpdateLocation() {
        selectLocationModeInteractor.enableAutoUpdateLocation()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}