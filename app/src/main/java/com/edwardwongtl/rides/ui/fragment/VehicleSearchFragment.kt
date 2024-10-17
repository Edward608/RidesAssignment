package com.edwardwongtl.rides.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.edwardwongtl.rides.R
import com.edwardwongtl.rides.databinding.FragmentVehicleSearchBinding
import com.edwardwongtl.rides.ui.VehicleListAdapter
import com.edwardwongtl.rides.ui.VerticalSpacingItemDecoration
import com.edwardwongtl.rides.viewmodel.ErrorType
import com.edwardwongtl.rides.viewmodel.SearchState
import com.edwardwongtl.rides.viewmodel.SortOption
import com.edwardwongtl.rides.viewmodel.VehicleSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VehicleSearchFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentVehicleSearchBinding
    private val viewmodel by viewModels<VehicleSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleSearchBinding.inflate(layoutInflater, container, false)

        with(binding) {
            lifecycleOwner = this@VehicleSearchFragment
            viewmodel = this@VehicleSearchFragment.viewmodel

            vehicleList.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            vehicleList.addItemDecoration(
                VerticalSpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.spacing_small
                    )
                )
            )

            searchButton.setOnClickListener {
                // Hide keyboard
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
                        SearchState.Loading -> hideError()
                        is SearchState.Error -> showError(it.errorType)
                        is SearchState.Success -> {
                            val adapter = VehicleListAdapter(it.result, viewLifecycleOwner) {
                                findNavController().navigate(
                                    R.id.vehicleDetailFragment,
                                    VehicleDetailFragmentArgs(it).toBundle(),
                                    navOptions {
                                        anim {
                                            enter = R.anim.slide_in
                                            exit = R.anim.slide_out
                                            popEnter = R.anim.slide_in
                                            popExit = R.anim.slide_out
                                        }
                                    }
                                )
                            }
                            binding.vehicleList.adapter = adapter
                        }
                    }
                }
            }
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner)

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

            // Network errors are not shown to the user for base_requirements
            // Add error handling for network errors for UX improvement
            else -> {}
        }
    }

    fun hideError() {
        binding.textInputLayout.error = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_sort, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> {
                val view = requireActivity().findViewById<View>(R.id.action_sort)
                showSortMenu(view)
                return true
            }

            else -> return false
        }
    }

    fun showSortMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_sort_options, popupMenu.menu)

        popupMenu.menu.findItem(R.id.sort_vin).isChecked =
            viewmodel.sortOption.value == SortOption.VIN
        popupMenu.menu.findItem(R.id.sort_car_type).isChecked =
            viewmodel.sortOption.value == SortOption.CAR_TYPE

        popupMenu.setOnMenuItemClickListener {
            lifecycleScope.launch {
                when (it.itemId) {
                    R.id.sort_vin -> viewmodel.updateSort(SortOption.VIN)
                    R.id.sort_car_type -> viewmodel.updateSort(SortOption.CAR_TYPE)
                    else -> {}
                }
            }
            true
        }
        popupMenu.show()
    }
}