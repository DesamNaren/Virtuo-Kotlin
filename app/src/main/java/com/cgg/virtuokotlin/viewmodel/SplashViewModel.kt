package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.source.VersionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SplashViewModel : ViewModel() {

    fun get(): LiveData<Response<VersionResponse>> = liveData {
        emit( LoginRepository().callVersionAPI())
    }
}