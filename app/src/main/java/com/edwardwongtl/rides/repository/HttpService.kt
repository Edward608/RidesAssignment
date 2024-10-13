package com.edwardwongtl.rides.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class HttpService {
    companion object {
        private const val BASE_URL = "https://random-data-api.com"

        private val okHttpClient by lazy { OkHttpClient() }

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        fun getVehicleService(): RandomVehicleApi = retrofit.create()
    }
}