package com.edwardwongtl.rides.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardwongtl.rides.repository.HttpService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehicleSearchViewModel : ViewModel() {
    val searchCount = MutableStateFlow<String>("")

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String> = _error

    fun getVehicles() {
        viewModelScope.launch {
            val input = searchCount.value
            if (searchCount.value.isEmpty()){
                setError(input)
                return@launch
            }

            val count = input.toInt()
            val service = HttpService.getVehicleService()
            val response = service.getVehicles(count)
            Log.i("VehicleSearchViewModel", response.toString())
            Log.i("VehicleSearchViewModel", response.body().toString())
        }
    }

    fun setError(input: String) {

    }
}