package com.github.zharovvv.open.source.weather.app.presentation.today.weather

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentWeatherTodayBinding
import com.github.zharovvv.open.source.weather.app.domain.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import com.github.zharovvv.open.source.weather.app.presentation.choose.city.navigateToChooseCity
import com.github.zharovvv.open.source.weather.app.presentation.error.showError
import com.github.zharovvv.open.source.weather.app.util.getColorFromAttr

class WeatherTodayFragment : BaseFragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentWeatherTodayBinding? = null
    private val binding: FragmentWeatherTodayBinding get() = _binding!!
    private val weatherTodayViewModel: WeatherTodayViewModel by viewModels { multiViewModelFactory }
    private val locationViewModel: LocationViewModel by activityViewModels { multiViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherTodayBinding.inflate(inflater, container, false)
        with(binding.weatherTodaySwipeRefreshLayout) {
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
        super.onViewCreated(view, savedInstanceState)
        bindHeader(
            viewBinding = binding,
            data = locationViewModel.locationData
        )
        bindWeatherTodayWidget(
            viewBinding = binding,
            data = weatherTodayViewModel.weatherTodayData
        )
        bindHourlyWeatherWidget(
            viewBinding = binding,
            data = weatherTodayViewModel.hourlyWeatherData
        )
        with(binding.weatherTodaySwipeRefreshLayout) {
            setOnRefreshListener {
                refreshData()
            }
        }
    }

    private fun bindHeader(
        viewBinding: FragmentWeatherTodayBinding,
        data: LiveData<LocationModel>,
    ) {
        viewBinding.weatherTodayHeaderContainer.setOnClickListener {
            navigateToChooseCity()
        }
        data.observe(viewLifecycleOwner) { locationModel: LocationModel ->
            with(viewBinding) {
                cityNameTextView.text = getString(R.string.delimiter_comma, locationModel.cityName)
                countryNameTextView.text = locationModel.countryName
            }
        }
    }

    private fun bindWeatherTodayWidget(
        viewBinding: FragmentWeatherTodayBinding,
        data: LiveData<DataState<WeatherTodayModel>>,
    ) {
        val detailedWeatherParamsLayoutManager = GridLayoutManager(context, 2)
        val detailedWeatherParamsAdapter = DetailedWeatherParamsAdapter()
        with(viewBinding.detailedParamsRecyclerView) {
            layoutManager = detailedWeatherParamsLayoutManager
            adapter = detailedWeatherParamsAdapter
        }
        data.observe(viewLifecycleOwner) { dataState: DataState<WeatherTodayModel> ->
            with(viewBinding) {
                when (dataState) {
                    is DataState.Success -> {
                        val weatherTodayModel = dataState.data
                        weatherTodayProgressBar.isVisible = false
                        weatherTodayCardIconImageView.setImageResource(weatherTodayModel.iconId)
                        weatherDescriptionTextView.text = weatherTodayModel.description
                        todayDateTextView.text = weatherTodayModel.dateString
                        weatherTodayTemperatureTextView.text = weatherTodayModel.temperature
                        detailedWeatherParamsAdapter.submitList(weatherTodayModel.detailedWeatherParams)
                        weatherTodaySwipeRefreshLayout.isRefreshing = false
                    }
                    is DataState.Loading -> {
                        weatherTodayProgressBar.isVisible = true
                    }
                    is DataState.Error -> {
                        handler.postDelayed({
                            weatherTodayProgressBar.isVisible = false
                            weatherTodaySwipeRefreshLayout.isRefreshing = false
                            showError(
                                errorModel = dataState,
                                errorContainerId = viewBinding.fragmentWeatherTodayErrorContainer.id,
                                onHideErrorListener = { _, _ ->
                                    refreshData()
                                }
                            )
                        }, 300L)
                    }
                }
            }
        }
    }

    private fun bindHourlyWeatherWidget(
        viewBinding: FragmentWeatherTodayBinding,
        data: LiveData<DataState<HourlyWeatherModel>>,
    ) {
        val navController = findNavController()
        val hourlyWeatherAdapter = HourlyWeatherAdapter()
        val hourlyWeatherLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val hourlyWeatherItemOnScrollListener = HourlyWeatherItemOnScrollListener()
        with(viewBinding) {
            toWeekWeatherButton.setOnClickListener {
                navController.navigate(R.id.action_nav_weather_today_to_nav_week_weather)
            }
            hourlyWeatherRecyclerView.layoutManager = hourlyWeatherLayoutManager
            hourlyWeatherRecyclerView.adapter = hourlyWeatherAdapter
            hourlyWeatherRecyclerView.addOnScrollListener(hourlyWeatherItemOnScrollListener)
        }
        hourlyWeatherItemOnScrollListener.timeIndicatorData.observe(viewLifecycleOwner) {
            with(viewBinding) {
                timeIndicatorTextView.text = it.data.description
                if (!it.isFirstValue) {
                    timeIndicatorTextView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                }
            }
        }
        data.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    hourlyWeatherAdapter.submitList(dataState.data.items)
                }
                is DataState.Error -> {
                    handler.postDelayed({
                        showError(
                            errorModel = dataState,
                            errorContainerId = viewBinding.fragmentWeatherTodayErrorContainer.id,
                            onHideErrorListener = { _, _ ->
                                refreshData()
                            }
                        )
                    }, 300L)
                }
                is DataState.Loading -> {
                }
            }
        }
    }

    private fun refreshData() {
        weatherTodayViewModel.requestTodayWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}