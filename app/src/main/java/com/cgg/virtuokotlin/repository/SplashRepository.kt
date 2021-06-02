package com.cgg.virtuokotlin.repository

import com.cgg.virtuokotlin.network.ViruoNetwork
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Response

class SplashRepository(private val vService: ViruoNetwork) {

    suspend fun callVersionAPI(): Response<VersionResponse> {
        return vService.getVersionCheck()
    }
}