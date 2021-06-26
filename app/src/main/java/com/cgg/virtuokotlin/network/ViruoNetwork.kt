/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cgg.virtuokotlin.network

import android.util.Log
import com.cgg.virtuokotlin.BuildConfig
import com.cgg.virtuokotlin.source.CoOrdinatesResponse
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.source.VersionResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

//private val service: ViruoNetwork {
//    val okHttpClient = OkHttpClient.Builder()
//        .readTimeout(60, TimeUnit.SECONDS)
//        .writeTimeout(60, TimeUnit.SECONDS)
//        .connectTimeout(60, TimeUnit.SECONDS)
//        .build()
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl(VIRTUO_BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    Log.i(TAG, ViruoNetwork::class.java.hashCode().toString())
//    retrofit.create(ViruoNetwork::class.java)
//}

//fun getNetworkService() = service

interface ViruoNetwork {

    @GET("web/getCurrentAppVersion2")
    suspend fun getVersionCheck(): Response<VersionResponse>

    @POST("web/userVerification")
    suspend fun getLoginResponse(
        @Header("userName") username: String?,
        @Header("password") password: String?,
        @Header("mobileNumber") mobNum: String?,
        @Header("deviceId") deviceId: String?,
        @Header("IMEI") IMEI: String?,
        @Header("fcmtoken") fcmtoken: String?,
        @Header("deviceType") deviceType: String?
    ): Response<LoginResponse>

    @GET("getOfficeCoordinates")
    suspend fun getOfficeCoOrdinates(@Header("Auth_Token") token: String): Response<CoOrdinatesResponse>
}

