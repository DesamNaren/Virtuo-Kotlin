package com.cgg.virtuokotlin.repository

import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    suspend fun callLoginAPI(loginReq: LoginReq): Response<LoginResponse> {
        val vService = getNetworkService()
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

    suspend fun callVersionAPI(): Response<VersionResponse> {
        val vService = getNetworkService()
        return vService.getVersionCheck()
    }
}