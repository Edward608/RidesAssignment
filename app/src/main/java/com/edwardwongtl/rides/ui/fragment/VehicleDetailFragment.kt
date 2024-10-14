package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.edwardwongtl.rides.R
import com.edwardwongtl.rides.databinding.FragmentVehicleDetailBinding
import com.edwardwongtl.rides.databinding.FragmentVehicleSearchBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        }

        return binding.root
    }
}