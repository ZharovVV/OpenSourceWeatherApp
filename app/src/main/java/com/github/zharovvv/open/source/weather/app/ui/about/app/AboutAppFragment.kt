package com.github.zharovvv.open.source.weather.app.ui.about.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.zharovvv.open.source.weather.app.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentAboutAppBinding? = null
    private val binding: FragmentAboutAppBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}