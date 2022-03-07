package com.github.zharovvv.open.source.weather.app.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.models.domain.PreferenceModel
import com.github.zharovvv.open.source.weather.app.models.domain.ThemeMode
import com.github.zharovvv.open.source.weather.app.resource.ResourceProvider
import com.github.zharovvv.open.source.weather.app.util.associateWithAnother
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

//TODO Переделать (очень сложная логика)
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

    //---------------------Автообновление местоположения----------------------------------------------
    private val preferenceLocationAutoUpdateKey: String =
        resourceProvider.getString(R.string.preference_location_auto_update_key)

    init {
        setDefaultValuesIfNone()
    }

    override fun preferencesChangesObservable(): Observable<PreferenceModel> {
        return Observable.create { emitter ->
            val onSharedPreferenceChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    val preferenceModel: PreferenceModel? = when (key) {
                        preferenceThemeKey -> requestThemePreference()
                        preferenceAutoUpdateKey -> requestAutoUpdatePreference()
                        preferenceAutoUpdateKey -> PreferenceModel.SimplePreferenceModel(
                            key = key,
                            name = "",
                            value = sharedPreferences.getString(key, null)!!
                        )
                        else -> null
                    }
                    if (!emitter.isDisposed) {
                        preferenceModel?.let { emitter.onNext(it) }
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

    override fun requestSimplePreference(key: String): PreferenceModel.SimplePreferenceModel? {
        val value: String? = sharedPreferences.getString(key, null)
        return value?.let {
            PreferenceModel.SimplePreferenceModel(
                key = key,
                name = it,
                value = value
            )
        }
    }

    override fun updateSimplePreference(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
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