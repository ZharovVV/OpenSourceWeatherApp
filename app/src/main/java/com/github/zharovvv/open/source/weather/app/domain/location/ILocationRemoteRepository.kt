package com.github.zharovvv.open.source.weather.app.domain.location

import android.Manifest
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Observable
import io.reactivex.Single

interface ILocationRemoteRepository {

    /**
     * В зависимости от [safeRequest]:
     * * Если safeRequest == false метод выбрасывает [IllegalStateException], если отсутствует пермишен
     * [Manifest.permission.ACCESS_COARSE_LOCATION]
     * * Если safeRequest == true создает [Observable.empty]
     */
    fun requestRealLocation(safeRequest: Boolean = false): Observable<LocationModel>

    fun findLocationByName(locationName: String, maxResults: Int = 5): Single<List<LocationModel>>
}