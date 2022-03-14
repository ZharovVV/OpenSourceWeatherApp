package com.github.zharovvv.open.source.weather.app.presentation.select.location.mode

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.FragmentSelectLocationModeBinding
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import com.github.zharovvv.open.source.weather.app.presentation.MainFragment
import com.github.zharovvv.open.source.weather.app.presentation.choose.city.navigateToChooseCity

class SelectLocationModeFragment : BaseFragment() {

    private var _binding: FragmentSelectLocationModeBinding? = null
    private val binding: FragmentSelectLocationModeBinding get() = _binding!!
    private val selectLocationModeViewModel: SelectLocationModeViewModel by viewModels {
        multiViewModelFactory
    }
    private var requestLocationPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectLocationModeBinding.inflate(inflater, container, false)
        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                selectLocationModeViewModel.enableAutoUpdateLocation()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseCityButton.setOnClickListener {
            navigateToChooseCity()
        }
        binding.autoUpdateLocationButton.setOnClickListener {
            requestLocationPermissionLauncher?.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        selectLocationModeViewModel.locationModeSelected.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<MainFragment>(
                    containerViewId = R.id.main_fragment_container
                )
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        requestLocationPermissionLauncher = null
        super.onDestroyView()
    }
}