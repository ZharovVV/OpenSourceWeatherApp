package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import io.reactivex.Observable

interface IPreferencesRepository {

    fun preferencesChangesObservable(): Observable<PreferenceModel>

    fun requestThemePreference(): PreferenceModel.ThemePreferenceModel

    fun requestAutoUpdatePreference(): PreferenceModel.AutoUpdatePreferenceModel

}