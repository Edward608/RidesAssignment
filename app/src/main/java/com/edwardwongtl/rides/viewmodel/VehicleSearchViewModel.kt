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

    private val _uiState = MutableStateFlow<SearchState>(SearchState.Empty)
    val uiState: StateFlow<SearchState> = _uiState

    val sortOption = MutableStateFlow<SortOption>(SortOption.VIN)

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
                when {
                    body == null -> _uiState.emit(SearchState.Error(ErrorType.NetworkError))
                    body.isEmpty() -> _uiState.emit(SearchState.Empty)
                    else -> {
                        when (sortOption.value) {
                            SortOption.VIN -> _uiState.emit(SearchState.Success(body.sortedBy { it.vin }))
                            SortOption.CAR_TYPE -> _uiState.emit(SearchState.Success(body.sortedBy { it.carType }))
                        }
                    }
                }
            } else {
                _uiState.emit(SearchState.Error(ErrorType.NetworkError))
            }
        }
    }

    fun updateSort(sortOption: SortOption) {
        val data = _uiState.value.result
        viewModelScope.launch {
            when (sortOption) {
                SortOption.VIN -> _uiState.emit(SearchState.Success(data.sortedBy { it.vin }))
                SortOption.CAR_TYPE -> _uiState.emit(SearchState.Success(data.sortedBy { it.carType }))
            }
            this@VehicleSearchViewModel.sortOption.emit(sortOption)
        }
    }
}

sealed class SearchState(
    open val errorType: ErrorType? = null,
    open val result: List<Vehicle> = emptyList(),
) {
    fun isEmpty(): Boolean = this is Empty
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    object Empty : SearchState()
    object Loading : SearchState()
    data class Error(override val errorType: ErrorType) : SearchState()
    data class Success(override val result: List<Vehicle>) : SearchState()
}

sealed interface ErrorType {
    object EmptyInput : ErrorType
    object InvalidInput : ErrorType
    object NetworkError : ErrorType
}

enum class SortOption {
    VIN,
    CAR_TYPE
}