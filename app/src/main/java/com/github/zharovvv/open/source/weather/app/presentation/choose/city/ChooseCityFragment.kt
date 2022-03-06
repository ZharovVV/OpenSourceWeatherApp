package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentChooseCityBinding
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityAutoUpdateBannerItem
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityItem
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import com.github.zharovvv.open.source.weather.app.util.adapter.CompositeAdapter

class ChooseCityFragment : BaseFragment() {

    private var _binding: FragmentChooseCityBinding? = null
    private val binding: FragmentChooseCityBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChooseCityBinding.inflate(inflater, container, false)
        binding.fragmentChooseCityToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.fragmentChooseCityToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val compositeAdapter = CompositeAdapter.Builder()
            .add(ChooseCityAdapter())
            .add(ChooseCityAutoUpdateBannerAdapter())
            .build()
        binding.chooseCityRecyclerView.adapter = compositeAdapter
        binding.chooseCityRecyclerView.layoutManager = LinearLayoutManager(context)
        val testChooseCityList = mutableListOf(
            ChooseCityAutoUpdateBannerItem(),
            ChooseCityItem(
                locationModel = LocationModel(
                    latitude = 0.0f,
                    longitude = 0.0f,
                    cityName = "Екатеринбург",
                    countryName = "Росиия"
                )
            ),
            ChooseCityItem(
                locationModel = LocationModel(
                    latitude = 0.0f,
                    longitude = 0.0f,
                    cityName = "Париж",
                    countryName = "Франция"
                )
            ),
            ChooseCityItem(
                locationModel = LocationModel(
                    latitude = 0.0f,
                    longitude = 0.0f,
                    cityName = "Нью-Йорк",
                    countryName = "США"
                )
            )
        )
        compositeAdapter.submitList(testChooseCityList)

        if (binding.searchCityEditText.requestFocus()) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.searchCityEditText,
                InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onDestroyView() {
        _binding = null
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive && requireActivity().currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken,
                0)
        }
        super.onDestroyView()
    }
}

fun BaseFragment.navigateToChooseCity() {
    val mainActivity = requireActivity()
    val mainFragment = mainActivity.supportFragmentManager.findFragmentByTag("mainFragment")!!
    mainActivity.supportFragmentManager.commit {
        setReorderingAllowed(true)
        remove(mainFragment)
        add<ChooseCityFragment>(
            containerViewId = R.id.main_fragment_container
        )
        addToBackStack(null)
    }
}