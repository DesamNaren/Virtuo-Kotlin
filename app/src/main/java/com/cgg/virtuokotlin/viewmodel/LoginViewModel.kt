package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse

class LoginViewModel : ViewModel() {
    var loginRes: MutableLiveData<LoginResponse>? = null

    fun getLoginResponse(loginReq: LoginReq): LiveData<LoginResponse>? {
        loginRes = LoginRepository.callLoginAPI(loginReq)
        return loginRes
    }
}