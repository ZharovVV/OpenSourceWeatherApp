package com.github.zharovvv.open.source.weather.app.ui.weather.today

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentWeatherTodayBinding
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult.CANCEL
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult.SHOULD_REQUEST_LOCATION_PERMISSION
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.navigation.LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
import com.github.zharovvv.open.source.weather.app.navigation.setUpNavDialogResultCallback
import com.github.zharovvv.open.source.weather.app.ui.BaseFragment

class WeatherTodayFragment : BaseFragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentWeatherTodayBinding? = null
    private val binding: FragmentWeatherTodayBinding get() = _binding!!
    private var requestLocationPermissionLauncher: ActivityResultLauncher<String>? = null
    private val locationViewModel: LocationViewModel by activityViewModels()
    private val weatherTodayViewModel: WeatherTodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        with(binding) {
            buttonToWeekWeather.setOnClickListener {
                navController.navigate(R.id.action_nav_weather_today_to_nav_week_weather)
            }
        }
        configureRequestLocationPermission(navController)
        locationViewModel.locationData.observe(viewLifecycleOwner) { locationModel: LocationModel ->
            binding.textViewTest1.text = "${locationModel.cityName}, ${locationModel.countryName}"
        }
        if (savedInstanceState == null && !isRestoredFromBackStack) {
            requestLocation()
        }
    }

    private fun configureRequestLocationPermission(navController: NavController) {
        requestLocationPermissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                requestLocation()
            } else {
                binding.textViewTest1.text = "Доступ к локации не предоставлен"
            }
        }
        setUpNavDialogResultCallback(
            navBackStackEntry = navController.getBackStackEntry(R.id.nav_weather_today),
            resultKey = LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
        ) { result: DialogResult ->
            when (result) {
                CANCEL -> {
                    navController.navigate(R.id.nav_location_permission_explanation)
                }

                SHOULD_REQUEST_LOCATION_PERMISSION -> {
                    requestLocationPermissionLauncher?.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }

    private fun requestLocation() {
        if (checkLocationPermission()) {
            locationViewModel.requestLocation()
        }
    }

    private fun checkLocationPermission(): Boolean {
        val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                locationPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            requireActivity().shouldShowRequestPermissionRationale(locationPermission) -> {
                findNavController().navigate(R.id.nav_location_permission_explanation)
            }

            else -> {
//                requestLocationPermissionLauncher?.launch(locationPermission)
                findNavController().navigate(R.id.nav_location_permission_explanation)
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requestLocationPermissionLauncher = null
    }
}