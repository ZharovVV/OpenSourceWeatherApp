package com.github.zharovvv.open.source.weather.app.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.navigation.AppToolbarOnDestinationChangedListener
import com.github.zharovvv.open.source.weather.app.navigation.setUpWithNavControllerCustom
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.main_drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavControllerByFragmentManager()
        navigationView.setUpWithNavControllerCustom(navController)
        navigationView.observeLocation(locationViewModel.locationData)
        toolbar.setupWithNavController(navController, drawerLayout)
        navController.addOnDestinationChangedListener(AppToolbarOnDestinationChangedListener(toolbar))
    }

    /**
     * Следует использовать данный метод вместо [findNavController], когда представление фрагмента
     * недоступно в момент вызова (например в Activity#onCreate).
     */
    private fun FragmentActivity.findNavControllerByFragmentManager(): NavController {
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        return navHostFragment.navController
    }

    private fun NavigationView.observeLocation(locationLiveData: LiveData<LocationModel>) {
        val navigationHeader = getHeaderView(0)
        val textViewNavHeaderLocation: TextView =
            navigationHeader.findViewById(R.id.nav_header_location_text_view)
        locationLiveData.observe(this@MainActivity) { locationModel: LocationModel ->
            textViewNavHeaderLocation.text = locationModel.cityName
        }
    }
}