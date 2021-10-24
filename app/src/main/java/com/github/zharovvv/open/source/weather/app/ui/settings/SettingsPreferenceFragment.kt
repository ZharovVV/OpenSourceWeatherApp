package com.github.zharovvv.open.source.weather.app.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.zharovvv.open.source.weather.app.R

class SettingsPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val listPreference: ListPreference? = findPreference("app_theme") as ListPreference?
        val themeCodeArray = resources.getStringArray(R.array.preference_theme_value_entries)
        val dayThemeCode = themeCodeArray[0]
        val nightThemeCode = themeCodeArray[1]
        val defaultThemeCode = themeCodeArray[2]
        val themeNameArray = resources.getStringArray(R.array.preference_theme_name_entries)
        val dayThemeName = themeNameArray[0]
        val nightThemeName = themeNameArray[1]
        val defaultThemeName = themeNameArray[2]
        listPreference?.summary =
            when (preferenceManager.sharedPreferences.getString("app_theme", defaultThemeCode)) {
                dayThemeCode -> dayThemeName
                nightThemeCode -> nightThemeName
                else -> defaultThemeName
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            val themeCodeArray = resources.getStringArray(R.array.preference_theme_value_entries)
            val dayThemeCode = themeCodeArray[0]
            val nightThemeCode = themeCodeArray[1]
            val defaultThemeCode = themeCodeArray[2]
            when (sharedPreferences.getString("app_theme", defaultThemeCode)) {
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
    }
}