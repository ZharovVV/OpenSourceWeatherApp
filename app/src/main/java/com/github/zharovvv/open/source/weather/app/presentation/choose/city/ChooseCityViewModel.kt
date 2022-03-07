package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.location.ChooseCityInteractor
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityItem
import com.github.zharovvv.open.source.weather.app.models.presentation.DelegateAdapterItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ChooseCityViewModel(
    private val chooseCityInteractor: ChooseCityInteractor,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var _realCityLocationOrBanner: DelegateAdapterItem? = null
    private var _citiesFoundByName: List<ChooseCityItem> = listOf()
    private val _chooseCityData = MutableLiveData<List<DelegateAdapterItem>>()
    val chooseCityData: LiveData<List<DelegateAdapterItem>> get() = _chooseCityData

    init {
        chooseCityInteractor.requestRealLocationOrBanner()
            .map { it.apply { _realCityLocationOrBanner = it } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _chooseCityData.value = listOf(it) + _citiesFoundByName
            }, Functions.emptyConsumer())
            .addTo(compositeDisposable)
    }

    fun chooseCity(chooseCityItem: ChooseCityItem) {
        Single.fromCallable { chooseCityInteractor.chooseCity(chooseCityItem) }
            .subscribeOn(Schedulers.io())
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun findCitiesByName(cityName: String) {
        chooseCityInteractor.findCitiesByName(cityName)
            .map { it.apply { _citiesFoundByName = it } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val realCityLocationOrBanner = _realCityLocationOrBanner
                val result = if (realCityLocationOrBanner != null) {
                    listOf(realCityLocationOrBanner) + it
                } else {
                    it
                }
                _chooseCityData.value = result
            }, Functions.emptyConsumer())
            .addTo(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}