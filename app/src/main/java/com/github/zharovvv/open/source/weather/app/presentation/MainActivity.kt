package com.github.zharovvv.open.source.weather.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.appComponent
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.presentation.select.location.mode.SelectLocationModeFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var preferencesRepository: IPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                if (preferencesRepository.requestSimplePreference("location_auto_update_key") == null) {
                    add<SelectLocationModeFragment>(containerViewId = R.id.main_fragment_container)
                } else {
                    add<MainFragment>(containerViewId = R.id.main_fragment_container)
                }
            }
        }
    }
}