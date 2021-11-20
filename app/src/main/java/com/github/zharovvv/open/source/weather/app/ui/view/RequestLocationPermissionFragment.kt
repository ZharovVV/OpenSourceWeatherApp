package com.github.zharovvv.open.source.weather.app.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.location.LocationPermissionExplanationDialogFragment
import com.github.zharovvv.open.source.weather.app.location.LocationService
import com.github.zharovvv.open.source.weather.app.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.navigation.LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
import com.github.zharovvv.open.source.weather.app.navigation.setUpNavDialogResultCallback

/**
 * Базовый класс, для фрагмента, в котором необходимо запрашивать Location Permission.
 * При предоставлении разрешения - запускает [LocationService].
 * Наблюдать за изменением местоположения можно через [LocationViewModel.locationData].
 * Для корректной работы при переопределении методов [onViewCreated] и [onDestroyView] в классах наследниках
 * необходимо вызывать методы супер-класса.
 */
abstract class RequestLocationPermissionFragment : BaseFragment() {

    private var requestLocationPermissionLauncher: ActivityResultLauncher<String>? = null
    protected val locationViewModel: LocationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        configureRequestLocationPermission(navController)
        //Вызываем метод только один раз при первом запуске фрагмента
//        if (savedInstanceState == null && !isRestoredFromBackStack) {
        requestLocation()
//        }
    }

    private fun configureRequestLocationPermission(navController: NavController) {
        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                requestLocation()
            } else {
                onLocationPermissionIsNotGranted(
                    DataState.Error.buildLocationPermissionError(
                        errorMessage = "Доступ к геоданнм не предоставлен!"
                    )
                )
            }
        }
        setUpNavDialogResultCallback(
            navBackStackEntry = navController.currentBackStackEntry!!,
            resultKey = LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
        ) { result: LocationPermissionExplanationDialogFragment.DialogResult ->
            when (result) {
                LocationPermissionExplanationDialogFragment.DialogResult.CANCEL -> {
                    navController.navigate(R.id.nav_location_permission_explanation)
                }

                LocationPermissionExplanationDialogFragment.DialogResult.SHOULD_REQUEST_LOCATION_PERMISSION -> {
                    requestLocationPermissionLauncher?.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }

    /**
     * Вызов данного метода запускает работу [LocationService], таким образом в [LocationViewModel.locationData]
     * начинают поступать актуальные данные о местоположении.
     */
    private fun requestLocation() {
        if (checkLocationPermission()) {
            locationViewModel.requestLocation()
        }
    }

    /**
     * Вызывается, когда пользователь запретил доступ к приложению.
     * TODO Возможно метод не нужен.
     */
    protected abstract fun onLocationPermissionIsNotGranted(errorModel: DataState.Error<LocationModel>)

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
                requestLocationPermissionLauncher?.launch(locationPermission)
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requestLocationPermissionLauncher = null
    }
}