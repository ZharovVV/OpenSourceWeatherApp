package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentChooseCityBinding
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import com.github.zharovvv.open.source.weather.app.util.adapter.CompositeAdapter
import com.github.zharovvv.open.source.weather.app.util.textObservable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class ChooseCityFragment : BaseFragment() {

    private var _binding: FragmentChooseCityBinding? = null
    private val binding: FragmentChooseCityBinding get() = _binding!!
    private val chooseCityViewModel: ChooseCityViewModel by viewModels { multiViewModelFactory }
    private var requestLocationPermissionLauncher: ActivityResultLauncher<String>? = null
    private val compositeDisposable = CompositeDisposable()

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
        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                chooseCityViewModel.enableAutoUpdateLocation()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val compositeAdapter = CompositeAdapter.Builder()
            .add(
                ChooseCityAdapter(
                    onItemClickListener = { chooseCityItem ->
                        chooseCityViewModel.chooseCity(chooseCityItem)
                        parentFragmentManager.popBackStack()
                    }
                )
            )
            .add(
                ChooseCityAutoUpdateBannerAdapter(
                    onItemClickListener = {
                        requestLocationPermissionLauncher?.launch(
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    }
                )
            )
            .build()
        binding.chooseCityRecyclerView.adapter = compositeAdapter
        binding.chooseCityRecyclerView.layoutManager = LinearLayoutManager(context)
        chooseCityViewModel.chooseCityData.observe(viewLifecycleOwner) {
            compositeAdapter.submitList(it)
        }

        if (binding.searchCityEditText.requestFocus()) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.searchCityEditText,
                InputMethodManager.SHOW_IMPLICIT)
        }
        binding.searchCityEditText.textObservable()
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .subscribe { chooseCityViewModel.findCitiesByName(it) }
            .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        _binding = null
        requestLocationPermissionLauncher = null
        compositeDisposable.clear()
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
    mainActivity.supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<ChooseCityFragment>(
            containerViewId = R.id.main_fragment_container
        )
        addToBackStack(null)
    }
}