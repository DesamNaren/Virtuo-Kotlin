package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cgg.virtuokotlin.Resource
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.source.LoginReq
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    fun callLoginAPI(loginReq: LoginReq) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.callLoginAPI(loginReq)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}