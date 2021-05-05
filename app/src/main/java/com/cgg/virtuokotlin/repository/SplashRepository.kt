package com.cgg.virtuokotlin.repository

import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Response

class SplashRepository {

    suspend fun callVersionAPI(): Response<VersionResponse> {
        val vService = getNetworkService()
        return vService.getVersionCheck()
    }
}