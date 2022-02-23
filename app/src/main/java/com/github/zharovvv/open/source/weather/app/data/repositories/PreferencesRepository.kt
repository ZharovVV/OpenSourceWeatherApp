package com.github.zharovvv.open.source.weather.app.data.repositories

import android.content.SharedPreferences
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import com.github.zharovvv.open.source.weather.app.models.domain.ThemeMode
import com.github.zharovvv.open.source.weather.app.resource.ResourceProvider
import com.github.zharovvv.open.source.weather.app.util.associateWithAnother
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

class PreferencesRepository(
    private val sharedPreferences: SharedPreferences,
    private val resourceProvider: ResourceProvider,
) : IPreferencesRepository {

    //----------------------------------Тема-------------------------------------------------
    private val preferenceThemeKey: String =
        resourceProvider.getString(R.string.preference_theme_key)
    private val possibleThemeValues: Array<String> =
        resourceProvider.getStringArray(R.array.preference_theme_value_entries)
    private val possibleThemeNames: Array<String> =
        resourceProvider.getStringArray(R.array.preference_theme_name_entries)
    private val themeValueNameMap: Map<String, String> =
        possibleThemeValues.associateWithAnother(valueArray = possibleThemeNames)

    //---------------------Период автообновления виджета погоды------------------------------
    private val preferenceAutoUpdateKey: String =
        resourceProvider.getString(R.string.preference_auto_update_key)
    private val possibleAutoUpdateValues: Array<String> =
        resourceProvider.getStringArray(R.array.preference_auto_update_value_entries)
    private val possibleAutoUpdateNames: Array<String> =
        resourceProvider.getStringArray(R.array.preference_auto_update_name_entries)
    private val autoUpdateValueNameMap: Map<String, String> =
        possibleAutoUpdateValues.associateWithAnother(valueArray = possibleAutoUpdateNames)

    init {
        setDefaultValuesIfNone()
    }

    override fun preferencesChangesObservable(): Observable<PreferenceModel> {
        return Observable.create { emitter ->
            val onSharedPreferenceChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    val preferenceModel: PreferenceModel = when (key) {
                        preferenceThemeKey -> requestThemePreference()
                        else -> requestAutoUpdatePreference()
                    }
                    if (!emitter.isDisposed) {
                        emitter.onNext(preferenceModel)
                    }
                }
            emitter.setDisposable(
                Disposables.fromAction {
                    sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                        onSharedPreferenceChangeListener
                    )
                }
            )
            sharedPreferences.registerOnSharedPreferenceChangeListener(
                onSharedPreferenceChangeListener
            )
        }
    }

    override fun requestThemePreference(): PreferenceModel.ThemePreferenceModel {
        val themePreferenceValue: String = sharedPreferences.getString(preferenceThemeKey, null)
            ?: throw NoSuchElementException()
        val themeName = themeValueNameMap.getValue(themePreferenceValue)
        val themeMode: ThemeMode = when (themePreferenceValue) {
            possibleThemeValues[0] -> ThemeMode.DAY
            possibleThemeValues[1] -> ThemeMode.NIGHT
            else -> ThemeMode.DEFAULT
        }
        return PreferenceModel.ThemePreferenceModel(
            key = preferenceThemeKey,
            name = themeName,
            value = themePreferenceValue,
            themeMode = themeMode
        )
    }

    override fun requestAutoUpdatePreference(): PreferenceModel.AutoUpdatePreferenceModel {
        val autoUpdatePreferenceValue: String =
            sharedPreferences.getString(preferenceAutoUpdateKey, null)
                ?: throw NoSuchElementException()
        return PreferenceModel.AutoUpdatePreferenceModel(
            key = preferenceAutoUpdateKey,
            name = autoUpdateValueNameMap.getValue(autoUpdatePreferenceValue),
            value = autoUpdatePreferenceValue,
            repeatIntervalInHours = autoUpdatePreferenceValue.toLong()
        )
    }

    private fun setDefaultValuesIfNone() {
        if (sharedPreferences.getString(preferenceThemeKey, null) == null) {
            val defaultThemeValue =
                resourceProvider.getStringArray(R.array.preference_theme_value_entries)[2]
            sharedPreferences.edit()
                .putString(preferenceThemeKey, defaultThemeValue)
                .apply()
        }
        if (sharedPreferences.getString(preferenceAutoUpdateKey, null) == null) {
            val defaultAutoUpdateValue =
                resourceProvider.getStringArray(R.array.preference_auto_update_value_entries)[0]
            sharedPreferences.edit()
                .putString(preferenceAutoUpdateKey, defaultAutoUpdateValue)
                .apply()
        }
    }
}