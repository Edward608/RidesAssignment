package com.edwardwongtl.rides.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardwongtl.rides.model.Vehicle
import com.edwardwongtl.rides.repository.HttpService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehicleSearchViewModel : ViewModel() {
    val searchCount = MutableStateFlow<String>("")

    private val _uiState = MutableStateFlow<SearchState>(SearchState.Success(emptyList()))
    val uiState: StateFlow<SearchState> = _uiState

    fun getVehicles() {
        viewModelScope.launch {
            val input = searchCount.value
            if (searchCount.value.isEmpty()) {
                _uiState.emit(SearchState.Error(ErrorType.EmptyInput))
                return@launch
            } else {
                _uiState.emit(SearchState.Loading)
            }

            val count = input.toInt()
            val service = HttpService.getVehicleService()
            val response = service.getVehicles(count)

            if (response.isSuccessful) {
                val body = response.body()
                _uiState.emit(SearchState.Success((body ?: emptyList()).sortedBy { it.vin }))
            } else {
                _uiState.emit(SearchState.Error(ErrorType.NetworkError))
            }
        }
    }
}

sealed interface SearchState {
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    object Loading : SearchState
    data class Error(val error: ErrorType) : SearchState
    data class Success(val result: List<Vehicle>) : SearchState
}

sealed interface ErrorType {
    object EmptyInput : ErrorType
    object InvalidInput : ErrorType
    object NetworkError : ErrorType
}