package com.example.water.api

import com.example.water.data.Info
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // tao service de giao tiep voi api
    private val BASE_URL="https://api.thingspeak.com"
    private val builder=Retrofit.Builder()
        .baseUrl(BASE_URL) // link api
        .addConverterFactory(GsonConverterFactory.create()) // factory de xu ly dang json
    val retrofit=builder.build()
    val apiService: InfoApi=retrofit.create(InfoApi::class.java)
}