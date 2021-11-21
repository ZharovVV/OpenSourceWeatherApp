package com.github.zharovvv.open.source.weather.app.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.ui.settings.PreferencesKeyProvider.preferenceAutoUpdateKey
import com.github.zharovvv.open.source.weather.app.ui.settings.PreferencesKeyProvider.preferenceThemeKey
import com.github.zharovvv.open.source.weather.app.util.associateWithAnother
import com.github.zharovvv.open.source.weather.app.widget.work.manager.schedulePeriodicWork

class SettingsPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        configureThemePreference()
        configureAutoUpdatePreference()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            when (key) {
                preferenceThemeKey -> {
                    handleOnThemePreferenceChange(sharedPreferences)
                }
                preferenceAutoUpdateKey -> {
                    handleOnAutoUpdatePreferenceChange()
                }
            }
        }
    }

    private fun configureThemePreference() {
        val listPreference: ListPreference? =
            findPreference(preferenceThemeKey) as ListPreference?
        val themeCodeArray: Array<String> =
            resources.getStringArray(R.array.preference_theme_value_entries)
        val themeNameArray = resources.getStringArray(R.array.preference_theme_name_entries)
        val codeNameMap: Map<String, String> =
            themeCodeArray.associateWithAnother(valueArray = themeNameArray)
        val defaultThemeCode = themeCodeArray[2]
        val themeCode = preferenceManager.sharedPreferences.getString(
            preferenceThemeKey,
            defaultThemeCode
        )
        listPreference?.summary = codeNameMap[themeCode]
    }

    private fun configureAutoUpdatePreference() {
        val listPreference: ListPreference? =
            findPreference(preferenceAutoUpdateKey) as ListPreference?
        val autoUpdateCodeArray: Array<String> =
            resources.getStringArray(R.array.preference_auto_update_value_entries)
        val autoUpdateNameArray =
            resources.getStringArray(R.array.preference_auto_update_name_entries)
        val codeNameMap: Map<String, String> =
            autoUpdateCodeArray.associateWithAnother(valueArray = autoUpdateNameArray)
        val defaultAutoUpdateCode = autoUpdateCodeArray[0]
        val autoUpdateCode = preferenceManager.sharedPreferences.getString(
            preferenceAutoUpdateKey,
            defaultAutoUpdateCode
        )
        listPreference?.summary = codeNameMap[autoUpdateCode]
    }

    private fun handleOnThemePreferenceChange(sharedPreferences: SharedPreferences) {
        val themeCodeArray = resources.getStringArray(R.array.preference_theme_value_entries)
        val dayThemeCode = themeCodeArray[0]
        val nightThemeCode = themeCodeArray[1]
        val defaultThemeCode = themeCodeArray[2]
        when (sharedPreferences.getString(preferenceThemeKey, defaultThemeCode)) {
            dayThemeCode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            nightThemeCode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        requireActivity().recreate()
    }

    private fun handleOnAutoUpdatePreferenceChange() {
        configureAutoUpdatePreference()
        schedulePeriodicWork(requireContext())
    }
}