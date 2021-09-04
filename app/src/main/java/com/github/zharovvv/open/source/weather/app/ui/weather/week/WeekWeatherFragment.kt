package com.github.zharovvv.open.source.weather.app.ui.weather.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.zharovvv.open.source.weather.app.databinding.FragmentWeekWeatherBinding
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.ui.BaseFragment

class WeekWeatherFragment : BaseFragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentWeekWeatherBinding? = null
    private val binding: FragmentWeekWeatherBinding get() = _binding!!
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding.toolbarFragmentWeekWeather.setupWithNavController(navController)
        locationViewModel.locationData.observe(viewLifecycleOwner) { locationModel: LocationModel ->
            binding.toolbarFragmentWeekWeather.title =
                "${locationModel.cityName}, ${locationModel.countryName}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}