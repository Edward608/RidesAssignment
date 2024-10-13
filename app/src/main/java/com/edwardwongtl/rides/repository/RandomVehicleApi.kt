package com.edwardwongtl.rides.repository

import com.edwardwongtl.rides.model.Vehicle
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomVehicleApi {
    @GET("api/vehicle/random_vehicle")
    suspend fun getVehicles(@Query("size") size: Int): Response<List<Vehicle>>
}