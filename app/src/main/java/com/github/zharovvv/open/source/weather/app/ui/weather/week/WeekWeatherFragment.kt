package com.github.zharovvv.open.source.weather.app.ui.weather.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentWeekWeatherBinding
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.ui.view.BaseFragment
import com.github.zharovvv.open.source.weather.app.util.getColorFromAttr

class WeekWeatherFragment : BaseFragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentWeekWeatherBinding? = null
    private val binding: FragmentWeekWeatherBinding get() = _binding!!
    private val locationViewModel: LocationViewModel by activityViewModels()
    private val weekWeatherViewModel: WeekWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekWeatherBinding.inflate(inflater, container, false)
        with(binding.weekWeatherSwipeRefreshLayout) {
            setColorSchemeColors(
                requireContext().getColorFromAttr(
                    R.attr.colorPrimary
                )
            )
            setProgressBackgroundColorSchemeColor(
                requireContext().getColorFromAttr(
                    R.attr.colorPrimaryDark
                )
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding.fragmentWeekWeatherToolbar.setupWithNavController(navController)
        val weekWeatherLayoutManager = LinearLayoutManager(context)
        val weekWeatherAdapter = WeekWeatherAdapter()
        with(binding.weekWeatherRecyclerView) {
            layoutManager = weekWeatherLayoutManager
            adapter = weekWeatherAdapter
        }
        locationViewModel.locationData.observe(viewLifecycleOwner) { locationModel: LocationModel ->
            binding.fragmentWeekWeatherToolbar.title =
                "${locationModel.cityName}, ${locationModel.countryName}"
        }
        weekWeatherViewModel.weatherTodayData.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    binding.weekWeatherProgressBar.isVisible = false
                    binding.weekWeatherSwipeRefreshLayout.isRefreshing = false
                    weekWeatherAdapter.submitList(dataState.data.items)
                }
                is DataState.Loading -> {
                    binding.weekWeatherProgressBar.isVisible = true
                }
                is DataState.Error -> {
                    binding.weekWeatherProgressBar.isVisible = false
                    binding.weekWeatherSwipeRefreshLayout.isRefreshing = false
                    binding.weekWeatherTitleTextView.text = dataState.message
                }
            }
        }
        with(binding.weekWeatherSwipeRefreshLayout) {
            setOnRefreshListener {
                weekWeatherViewModel.requestWeatherToday()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}