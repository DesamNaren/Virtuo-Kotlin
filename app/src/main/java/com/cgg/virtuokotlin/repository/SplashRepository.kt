package com.cgg.virtuokotlin.repository

import com.cgg.virtuokotlin.di.DaggerAppComponent
import com.cgg.virtuokotlin.network.ViruoNetwork
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Response
import javax.inject.Inject

class SplashRepository @Inject constructor(private val vService: ViruoNetwork) {

    suspend fun callVersionAPI(): Response<VersionResponse> {
        return vService.getVersionCheck()
    }
}