package com.github.zharovvv.open.source.weather.app.location

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.navigation.LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY
import com.github.zharovvv.open.source.weather.app.navigation.setResultForPreviousDestination

class LocationPermissionExplanationDialogFragment : DialogFragment() {

    enum class DialogResult {
        CANCEL,
        SHOULD_REQUEST_LOCATION_PERMISSION
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_fragment_location_permission_explanation,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button: Button = view.findViewById(R.id.button_request_location_permission)
        val navController = findNavController()
        button.setOnClickListener {
            navController.setResultForPreviousDestination(
                LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY,
                DialogResult.SHOULD_REQUEST_LOCATION_PERMISSION
            )
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        val navController = findNavController()
        navController.setResultForPreviousDestination(
            LOCATION_PERMISSION_EXPLANATION_DIALOG_RESULT_KEY,
            DialogResult.CANCEL
        )
    }
}