package com.edwardwongtl.rides.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardwongtl.rides.model.Vehicle
import com.edwardwongtl.rides.repository.HttpService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehicleSearchViewModel : ViewModel() {
    val searchCount = MutableStateFlow<String>("")

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchResult = MutableStateFlow<List<Vehicle>>(emptyList())
    val searchResult: StateFlow<List<Vehicle>> = _searchResult

    private val _uiState = MutableStateFlow<SearchState>(SearchState.Success(emptyList()))
    val uiState: StateFlow<SearchState> = _uiState

    fun getVehicles() {
        viewModelScope.launch {
            val input = searchCount.value
            if (searchCount.value.isEmpty()) {
                _error.emit("Empty input")
                return@launch
            } else {
                _error.emit(null)
            }

            val count = input.toInt()
            val service = HttpService.getVehicleService()
            val response = service.getVehicles(count)
        }
    }
}

sealed interface SearchState {
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    object Loading : SearchState
    data class Error(val error: String) : SearchState
    data class Success(val result: List<Vehicle>) : SearchState
}