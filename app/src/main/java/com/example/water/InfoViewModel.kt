package com.example.water

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalArgumentException

class InfoViewModel(application: Application): ViewModel() {
    private val infoRepository: InfoRepository= InfoRepository(application)

    fun getInfoFromApi1() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try{
            emit(Resource.success(infoRepository.getNotesFromApi1()))
        }catch (ex: Exception){
            emit(Resource.error(null,ex.message?:"Error"))
        }
    }
    fun getInfoFromApi2() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try{
            emit(Resource.success(infoRepository.getNotesFromApi2()))
        }catch (ex: Exception){
            emit(Resource.error(null,ex.message?:"Error"))
        }
    }

    class InfoViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(InfoViewModel::class.java)){
                return InfoViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewModel")
        }

    }
}