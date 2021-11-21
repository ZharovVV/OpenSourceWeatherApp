package com.github.zharovvv.open.source.weather.app.ui.about.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zharovvv.open.source.weather.app.databinding.FragmentAboutAppBinding
import com.github.zharovvv.open.source.weather.app.model.AboutAppModel
import com.github.zharovvv.open.source.weather.app.ui.view.BaseFragment

class AboutAppFragment : BaseFragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding: FragmentAboutAppBinding get() = _binding!!
    private val aboutAppViewModel: AboutAppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding.fragmentAboutAppToolbar.setupWithNavController(navController)
        val aboutAppLayoutManager = LinearLayoutManager(context)
        val aboutAppAdapter = AboutAppAdapter()
        with(binding.aboutAppRecyclerView) {
            layoutManager = aboutAppLayoutManager
            adapter = aboutAppAdapter
        }
        aboutAppViewModel.aboutAppData.observe(viewLifecycleOwner) { aboutAppModel: AboutAppModel ->
            aboutAppAdapter.submitList(aboutAppModel.parameters)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}