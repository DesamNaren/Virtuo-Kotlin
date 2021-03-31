package com.cgg.virtuokotlin.repository

import androidx.lifecycle.MutableLiveData
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object OTPRepository {
    private val loginRes = MutableLiveData<LoginResponse>()

    fun callLoginAPI(loginReq: LoginReq): MutableLiveData<LoginResponse> {
        if (loginRes.value != null) {
            return loginRes
        }
        val vService = getNetworkService()
        val call = vService.getLoginResponse(
            loginReq.userName,
            loginReq.password,
            loginReq.mobileNumber,
            loginReq.deviceId,
            loginReq.IMEI,
            loginReq.fcmtoken,
            AppConstants.ANDROID
        )

        call!!.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                loginRes.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                loginRes.value = null
            }

        })
        return loginRes
    }
}