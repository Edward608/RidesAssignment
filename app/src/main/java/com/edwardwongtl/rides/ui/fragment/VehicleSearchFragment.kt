package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.edwardwongtl.rides.MainActivity
import com.edwardwongtl.rides.R
import com.edwardwongtl.rides.databinding.FragmentVehicleSearchBinding
import com.edwardwongtl.rides.ui.VehicleListAdapter
import com.edwardwongtl.rides.ui.VerticalSpacingItemDecoration
import com.edwardwongtl.rides.viewmodel.ErrorType
import com.edwardwongtl.rides.viewmodel.SearchState
import com.edwardwongtl.rides.viewmodel.VehicleSearchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VehicleSearchFragment : Fragment() {
    private lateinit var binding: FragmentVehicleSearchBinding
    private val viewmodel by viewModels<VehicleSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleSearchBinding.inflate(layoutInflater, container, false)

        with(binding) {
            lifecycleOwner = this@VehicleSearchFragment
            viewmodel = this@VehicleSearchFragment.viewmodel
            (requireActivity() as MainActivity).setSupportActionBar(toolbar)

            vehicleResult.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            vehicleResult.addItemDecoration(
                VerticalSpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.spacing_small
                    )
                )
            )

            searchButton.setOnClickListener {
                val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
                this@VehicleSearchFragment.viewmodel.getVehicles()
            }
        }

        lifecycleScope.launch {
            viewmodel.uiState.collectLatest {
                withResumed {
                    when (it) {
                        SearchState.Empty -> {}
                        SearchState.Loading -> {}
                        is SearchState.Error -> showError(it.errorType)
                        is SearchState.Success -> {
                            val adapter = VehicleListAdapter(it.result, viewLifecycleOwner) {
                                findNavController().navigate(
                                    R.id.vehicleDetailFragment,
                                    VehicleDetailFragmentArgs(it).toBundle()
                                )
                            }
                            binding.vehicleResult.adapter = adapter
                        }
                    }
                }
            }
        }

        return binding.root
    }

    fun showError(errorType: ErrorType) {
        when (errorType) {
            ErrorType.EmptyInput -> {
                binding.textInputLayout.error = getString(R.string.error_empty_input)
            }

            ErrorType.InvalidInput -> {
                binding.textInputLayout.error = getString(R.string.error_invalid_input)
            }

            ErrorType.NetworkError -> {
                binding.textInputLayout.error = null
                Snackbar.make(
                    requireView(), getString(R.string.error_network), Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}