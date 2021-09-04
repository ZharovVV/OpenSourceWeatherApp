package com.github.zharovvv.open.source.weather.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.navigation.AppToolbarOnDestinationChangedListener
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        val drawerLayout: DrawerLayout = findViewById(R.id.main_drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavControllerByFragmentManager()
        navigationView.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, drawerLayout)
        navController.addOnDestinationChangedListener(AppToolbarOnDestinationChangedListener(toolbar))
    }

    /**
     * Следует использовать данный метод вместо [findNavController], когда представление фрагмента
     * недоступно в момент вызова (например в Activity#onCreate).
     */
    private fun FragmentActivity.findNavControllerByFragmentManager(): NavController {
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container
        ) as NavHostFragment
        return navHostFragment.navController
    }
}