package com.github.zharovvv.open.source.weather.app.domain.location

import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import io.reactivex.Observable

class SelectLocationModeInteractor(
    private val preferencesRepository: IPreferencesRepository,
) {

    fun enableAutoUpdateLocation() {
        preferencesRepository.updateSimplePreference(
            key = "location_auto_update_key",
            value = true.toString()
        )
    }

    fun autoUpdateLocationPreferencesChangesObservable(): Observable<PreferenceModel.SimplePreferenceModel> {
        return preferencesRepository.preferencesChangesObservable()
            .filter { it is PreferenceModel.SimplePreferenceModel }
            .filter { it.key == "location_auto_update_key" }
            .map { it as PreferenceModel.SimplePreferenceModel }
    }
}