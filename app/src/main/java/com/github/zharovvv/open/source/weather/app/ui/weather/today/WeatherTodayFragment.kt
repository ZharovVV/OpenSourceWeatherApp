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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentWeatherTodayBinding
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult.CANCEL
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment.DialogResult.SHOULD_REQUEST_LOCATION_PERMISSION
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.navigation.LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
import com.github.zharovvv.open.source.weather.app.navigation.setUpNavDialogResultCallback
import com.github.zharovvv.open.source.weather.app.ui.view.BaseFragment

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
        val detailedWeatherParamsAdapter = DetailedWeatherParamsAdapter()
        with(binding) {
            toWeekWeatherButton.setOnClickListener {
                navController.navigate(R.id.action_nav_weather_today_to_nav_week_weather)
            }
            detailedParamsRecyclerView.layoutManager =
                GridLayoutManager(this@WeatherTodayFragment.context, 2)
            detailedParamsRecyclerView.adapter = detailedWeatherParamsAdapter
        }
        configureRequestLocationPermission(navController)
        locationViewModel.locationData.observe(viewLifecycleOwner) { locationModel: LocationModel ->
            binding.cityNameTextView.text =
                getString(R.string.delimiter_comma, locationModel.cityName)
            binding.countryNameTextView.text = locationModel.countryName
        }
        weatherTodayViewModel.weatherTodayData.observe(viewLifecycleOwner) { dataState: DataState<WeatherTodayModel> ->
            when (dataState) {
                is DataState.Success -> {
                    val weatherTodayModel = dataState.data
                    with(binding) {
                        weatherTodayProgressBar.isVisible = false
                        weatherTodayCardIconImageView.setImageResource(weatherTodayModel.iconId)
                        weatherDescriptionTextView.text = weatherTodayModel.description
                        todayDateTextView.text = weatherTodayModel.dateString
                        weatherTodayTemperatureTextView.text = weatherTodayModel.temperature
                    }
                    detailedWeatherParamsAdapter.submitList(weatherTodayModel.detailedWeatherParams)
                }
                is DataState.Loading -> {
                    binding.weatherTodayProgressBar.isVisible = true
                }
                is DataState.Error -> {
                    binding.weatherTodayProgressBar.isVisible = false
                    binding.weatherDescriptionTextView.text = dataState.message
                }
            }
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
                binding.cityNameTextView.text = "Доступ к локации не предоставлен"
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