package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.edwardwongtl.rides.databinding.FragmentVehicleSearchBinding
import com.edwardwongtl.rides.viewmodel.VehicleSearchViewModel

class VehicleSearchFragment : Fragment() {
    private val viewmodel by viewModels<VehicleSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentVehicleSearchBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel
        return binding.root
    }
}