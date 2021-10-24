package com.github.zharovvv.open.source.weather.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.zharovvv.open.source.weather.app.databinding.FragmentSettingsContainerBinding
import com.github.zharovvv.open.source.weather.app.ui.view.BaseFragment

class SettingsContainerFragment : BaseFragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentSettingsContainerBinding? = null
    private val binding: FragmentSettingsContainerBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding.fragmentSettingsContainerToolbar.setupWithNavController(navController)
        parentFragmentManager
            .beginTransaction()
            .replace(binding.preferenceFragmentContainer.id, SettingsPreferenceFragment())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}