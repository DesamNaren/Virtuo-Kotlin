package com.cgg.virtuokotlin.repository

import androidx.lifecycle.MutableLiveData
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.source.VersionResponse
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SplashRepository {
    private val versionRes = MutableLiveData<VersionResponse?>()

    fun callVersionCheckAPI(): MutableLiveData<VersionResponse?> {
        if (versionRes.value != null) {
            return versionRes
        }
        val vService = getNetworkService()
        val call = vService.getVersionCheck()

        call!!.enqueue(object : Callback<VersionResponse?> {
            override fun onResponse(
                call: Call<VersionResponse?>,
                response: Response<VersionResponse?>
            ) {
                versionRes.value = response.body()
            }

            override fun onFailure(call: Call<VersionResponse?>, t: Throwable) {
                versionRes.value = null
            }

        })
        return versionRes
    }
}