package com.example.water

import android.app.Application
import com.example.water.api.ApiConfig

class InfoRepository(app: Application) {
    suspend fun getNotesFromApi1()=ApiConfig.apiService.getAllInfo1()
    suspend fun getNotesFromApi2()=ApiConfig.apiService.getAllInfo2()
}