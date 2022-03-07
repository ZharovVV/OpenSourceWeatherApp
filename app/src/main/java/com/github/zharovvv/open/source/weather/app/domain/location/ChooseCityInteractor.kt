package com.github.zharovvv.open.source.weather.app.domain.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityAutoUpdateBannerItem
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityItem
import com.github.zharovvv.open.source.weather.app.models.presentation.DelegateAdapterItem
import io.reactivex.Observable
import io.reactivex.Single

class ChooseCityInteractor(
    private val mutableLocationRepository: IMutableLocationRepository,
    private val locationRemoteRepository: ILocationRemoteRepository,
    private val preferencesRepository: IPreferencesRepository,
    private val applicationContext: Context,
) {

    fun chooseCity(chooseCityItem: ChooseCityItem) {
        mutableLocationRepository.updateLocation(chooseCityItem.locationModel)
    }

    fun findCitiesByName(cityName: String): Single<List<ChooseCityItem>> {
        return locationRemoteRepository
            .findLocationByName(
                locationName = cityName,
                maxResults = 5
            )
            .map { locationModelList ->
                locationModelList.map { ChooseCityItem(it) }
            }
    }

    fun requestRealLocationOrBanner(): Observable<DelegateAdapterItem> {
        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return locationRemoteRepository.requestRealLocation()
                .map { ChooseCityItem(it) }
        } else {
            return Observable.merge(
                Observable.just(ChooseCityAutoUpdateBannerItem()),
                preferencesRepository.preferencesChangesObservable()
                    .filter { preferenceModel -> preferenceModel.key == "location_auto_update_key" }
                    .filter { preferenceModel -> preferenceModel.value.toBoolean() }
                    .switchMap {
                        locationRemoteRepository.requestRealLocation().map { ChooseCityItem(it) }
                    }
            )
        }
    }
}