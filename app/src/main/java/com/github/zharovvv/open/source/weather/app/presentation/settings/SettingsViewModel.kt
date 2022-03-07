package com.github.zharovvv.open.source.weather.app.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import com.github.zharovvv.open.source.weather.app.models.domain.ThemeMode
import com.github.zharovvv.open.source.weather.app.util.MutableSingleLiveEvent
import com.github.zharovvv.open.source.weather.app.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingsViewModel(
    private val preferencesRepository: IPreferencesRepository,
    private val workManagerGateway: WorkManagerGateway,
) : ViewModel() {

    private val _themePreference = MutableLiveData(preferencesRepository.requestThemePreference())
    val themePreference: LiveData<PreferenceModel.ThemePreferenceModel> get() = _themePreference

    private val _autoUpdateWidgetPreference =
        MutableLiveData(preferencesRepository.requestAutoUpdatePreference())
    val autoUpdateWidgetPreference: LiveData<PreferenceModel.AutoUpdatePreferenceModel> get() = _autoUpdateWidgetPreference

    private val _themePreferenceChanges =
        MutableSingleLiveEvent<ThemeMode>()
    val themeModeChanges: SingleLiveEvent<ThemeMode> =
        _themePreferenceChanges

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += preferencesRepository.preferencesChangesObservable()
            .subscribe { preferenceModel: PreferenceModel ->
                when (preferenceModel) {
                    is PreferenceModel.ThemePreferenceModel -> {
                        _themePreferenceChanges.setValue(preferenceModel.themeMode)
                        _themePreference.value = preferenceModel
                    }
                    is PreferenceModel.AutoUpdatePreferenceModel -> {
                        _autoUpdateWidgetPreference.value = preferenceModel
                        workManagerGateway.schedulePeriodicWeatherWidgetUpdate()
                    }
                    else -> {}
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}