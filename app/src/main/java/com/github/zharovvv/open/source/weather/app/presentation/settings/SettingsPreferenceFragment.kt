package com.github.zharovvv.open.source.weather.app.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.appComponent
import com.github.zharovvv.open.source.weather.app.di.presentation.MultiViewModelFactory
import com.github.zharovvv.open.source.weather.app.models.domain.ThemeMode
import javax.inject.Inject

class SettingsPreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    internal lateinit var multiViewModelFactory: MultiViewModelFactory
    private val settingsViewModel: SettingsViewModel by viewModels { multiViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureThemePreference()
        configureAutoUpdatePreference()
        settingsViewModel.themeModeChanges.observe(viewLifecycleOwner) { themeMode: ThemeMode ->
            AppCompatDelegate.setDefaultNightMode(
                when (themeMode) {
                    ThemeMode.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
                    ThemeMode.DEFAULT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
            requireActivity().recreate()
        }
    }



    private fun configureThemePreference() {
        settingsViewModel.themePreference.observe(viewLifecycleOwner) { themePreferenceModel ->
            val themeListPreference = findPreference(themePreferenceModel.key) as ListPreference?
            themeListPreference?.summary = themePreferenceModel.name
        }
    }

    private fun configureAutoUpdatePreference() {
        settingsViewModel.autoUpdateWidgetPreference.observe(viewLifecycleOwner) { autoUpdatePreference ->
            val autoUpdateListPreference =
                findPreference(autoUpdatePreference.key) as ListPreference?
            autoUpdateListPreference?.summary = autoUpdatePreference.name
        }
    }
}