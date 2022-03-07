package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.domain.location.ILocationRemoteRepository
import com.github.zharovvv.open.source.weather.app.domain.location.IMutableLocationRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

class LocationRepository(
    private val locationLocalRepository: LocationLocalRepository,
    private val locationRemoteRepository: ILocationRemoteRepository,
    private val preferencesRepository: IPreferencesRepository,
) : IMutableLocationRepository {

    override fun updateLocation(locationModel: LocationModel) {
        preferencesRepository.updateSimplePreference(
            key = "location_auto_update_key",
            value = locationModel.isRealLocation.toString()
        )
        locationLocalRepository.updateLocation(locationModel)
    }

    override fun locationObservable(): Flowable<LocationModel> {
        val compositeDisposable = CompositeDisposable()
        val locationAutoUpdatePreference = preferencesRepository.requestSimplePreference(
            key = "location_auto_update_key"
        )
        var remoteLocationDisposable: Disposable? = null
        if (locationAutoUpdatePreference != null && locationAutoUpdatePreference.value.toBoolean()) {
            remoteLocationDisposable = locationRemoteRepository.requestRealLocation()
                .subscribe { locationLocalRepository.updateLocation(it) }
                .addTo(compositeDisposable)
        }
        preferencesRepository.preferencesChangesObservable()
            .filter { preferenceModel -> preferenceModel.key == "location_auto_update_key" }
            .subscribe { preferenceModel ->
                if (preferenceModel.value.toBoolean()) {
                    if (remoteLocationDisposable == null) {
                        remoteLocationDisposable =
                            locationRemoteRepository.requestRealLocation()
                                .subscribe { locationLocalRepository.updateLocation(it) }
                                .addTo(compositeDisposable)
                    }
                } else {
                    remoteLocationDisposable?.dispose()
                    remoteLocationDisposable = null
                }
            }
            .addTo(compositeDisposable)
        return locationLocalRepository.locationObservable()
            .doOnCancel { compositeDisposable.clear() }
    }

    override fun getLastKnownLocation(): LocationModel? {
        return locationLocalRepository.getLastKnownLocation()
    }
}