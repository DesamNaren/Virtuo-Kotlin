package com.cgg.virtuokotlin.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPViewModel : ViewModel() {
    var loginRes: MutableLiveData<LoginResponse>? = null

    fun getLoginResponse(loginReq: LoginReq): LiveData<LoginResponse>? {
        loginRes = LoginRepository.callLoginAPI(loginReq)
        return loginRes
    }
}