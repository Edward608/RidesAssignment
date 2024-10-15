package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.edwardwongtl.rides.MainActivity
import com.edwardwongtl.rides.databinding.FragmentVehicleDetailBinding

class VehicleDetailFragment : Fragment() {
    private val args by navArgs<VehicleDetailFragmentArgs>()
    private lateinit var binding: FragmentVehicleDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleDetailBinding.inflate(layoutInflater, container, false)
        with(binding) {
            lifecycleOwner = this@VehicleDetailFragment
            vehicle = args.vehicle
            (requireActivity() as MainActivity).apply {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                val navController = findNavController()
                val appConfiguration = AppBarConfiguration(navController.graph)
                toolbar.setupWithNavController(navController, appConfiguration)
            }
        }

        return binding.root
    }
}