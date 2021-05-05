package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun get(loginReq: LoginReq): LiveData<Response<LoginResponse>> = liveData {
        emit( LoginRepository().callLoginAPI(loginReq))
    }
}