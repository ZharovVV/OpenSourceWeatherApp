package com.github.zharovvv.open.source.weather.app.presentation.today.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.IHourlyWeatherRepository
import com.github.zharovvv.open.source.weather.app.domain.ILocationRepository
import com.github.zharovvv.open.source.weather.app.domain.IWeatherTodayRepository
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.presentation.widget.WeatherWidgetManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class WeatherTodayViewModel(
    private val locationRepository: ILocationRepository,
    private val weatherRepository: IWeatherTodayRepository,
    private val hourlyWeatherRepository: IHourlyWeatherRepository,
    private val widgetWeatherInteractor: WidgetWeatherInteractor,
    private val workManagerGateway: WorkManagerGateway,
    private val weatherWidgetManager: WeatherWidgetManager
) : ViewModel() {

    private val _weatherTodayData = MutableLiveData<DataState<WeatherTodayModel>>()
    val weatherTodayData: LiveData<DataState<WeatherTodayModel>> get() = _weatherTodayData
    private val _hourlyWeatherData = MutableLiveData<DataState<HourlyWeatherModel>>()
    val hourlyWeatherData: LiveData<DataState<HourlyWeatherModel>> get() = _hourlyWeatherData

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += locationRepository.locationObservable()
            .observeOn(Schedulers.io())
            .subscribe {
                weatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = false
                )
                hourlyWeatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = false
                )
            }
        compositeDisposable += weatherRepository.observableData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _weatherTodayData.value = it }
        compositeDisposable += hourlyWeatherRepository.observableData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _hourlyWeatherData.value = it }
        compositeDisposable += widgetWeatherInteractor.observableData()
            .observeOn(Schedulers.io())
            .filter { weatherWidgetManager.hasAnyWidget() }
            .subscribe(
                { widgetModelDataState ->
                    weatherWidgetManager.updateWidget(widgetModelDataState)
                    workManagerGateway.schedulePeriodicWeatherWidgetUpdate()
                },
                Functions.emptyConsumer()
            )
    }

    fun requestTodayWeather() {
        compositeDisposable += Single
            .fromCallable {
                locationRepository.getLastKnownLocation()
            }
            .subscribeOn(Schedulers.io())
            .filter { locationModel: LocationModel? -> locationModel != null }
            .map {
                weatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = true
                )
                hourlyWeatherRepository.requestData(
                    locationModel = it,
                    withLoadingStatus = true
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Functions.emptyConsumer(), { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}