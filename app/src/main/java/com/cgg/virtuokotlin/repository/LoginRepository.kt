package com.cgg.virtuokotlin.repository

import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.network.ViruoNetwork
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val vService: ViruoNetwork) {

    suspend fun callLoginAPI(loginReq: LoginReq): Response<LoginResponse> {
        return vService.getLoginResponse(
            loginReq.userName,
            loginReq.password,
            loginReq.mobileNumber,
            loginReq.deviceId,
            loginReq.IMEI,
            loginReq.fcmtoken,
            AppConstants.ANDROID
        )
    }
}