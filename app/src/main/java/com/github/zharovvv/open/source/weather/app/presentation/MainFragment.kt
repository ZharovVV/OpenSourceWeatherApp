package com.github.zharovvv.open.source.weather.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.NavHostFragment
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.navigation.custom.view.CustomBottomNavigationView
import com.github.zharovvv.open.source.weather.app.navigation.setUpWithNavControllerCustom

class MainFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView =
            view.findViewById<CustomBottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setUpWithNavControllerCustom(navController)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (navController.backQueue.size > 2) {
                navController.popBackStack()
            } else {
                requireActivity().finish()
            }
        }
    }
}