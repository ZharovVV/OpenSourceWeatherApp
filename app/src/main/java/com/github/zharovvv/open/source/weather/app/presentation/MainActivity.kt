package com.github.zharovvv.open.source.weather.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.navigation.custom.view.CustomBottomNavigationView
import com.github.zharovvv.open.source.weather.app.navigation.setUpWithNavControllerCustom

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavControllerByFragmentManager()
        val bottomNavigationView = findViewById<CustomBottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setUpWithNavControllerCustom(navController)
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
}