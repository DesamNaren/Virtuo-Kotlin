package com.cgg.virtuokotlin.network

import android.util.Log
import com.cgg.virtuokotlin.BuildConfig
import com.cgg.virtuokotlin.source.VersionResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

val VIRTUO_BASE_URL = BuildConfig.SERVER_URL
private const val TAG = "VirtuoService"

interface VirtuoService {

    object RetrofitInstance{

        fun createInstance(): VirtuoService {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(VIRTUO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            Log.i(TAG, RetrofitInstance::class.java.hashCode().toString())

            return retrofit.create(VirtuoService::class.java)
        }

    }

    @GET("web/getCurrentAppVersion2")
    fun getVersionCheck(): Call<VersionResponse>?
}