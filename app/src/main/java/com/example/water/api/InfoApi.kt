package com.example.water.api

import com.example.water.data.Info
import retrofit2.http.GET
import retrofit2.http.POST

interface InfoApi {
    // tao phuong thuc get den api
    @GET("/channels/1471591/fields/1.json?api_key=VNV7HZ6GRDMM6O3V&results=1")
    suspend fun getAllInfo1(): Info

    @GET("/channels/1471591/fields/2.json?api_key=VNV7HZ6GRDMM6O3V&results=1")
    suspend fun getAllInfo2(): Info
}