package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import com.edwardwongtl.rides.databinding.FragmentVehicleSearchBinding
import com.edwardwongtl.rides.viewmodel.VehicleSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            viewmodel.error.collectLatest {
                withResumed {
                    binding.textInputLayout.error = it
                }
            }
        }

        return binding.root
    }
}