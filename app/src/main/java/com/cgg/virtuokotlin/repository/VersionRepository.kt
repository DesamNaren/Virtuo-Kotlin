package com.cgg.virtuokotlin.repository

import androidx.lifecycle.MutableLiveData
import com.cgg.virtuokotlin.network.VirtuoService
import com.cgg.virtuokotlin.source.VersionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VersionRepository {
    private val versionRes = MutableLiveData<VersionResponse>()

    fun callVersionCheckAPI(): MutableLiveData<VersionResponse> {
        if (versionRes.value != null) {
            return versionRes
        }
        val virtuoService = VirtuoService.RetrofitInstance.createInstance()
        val call = virtuoService.getVersionCheck()

        call!!.enqueue(object : Callback<VersionResponse> {
            override fun onResponse(
                call: Call<VersionResponse>,
                response: Response<VersionResponse>
            ) {
                versionRes.value = response.body()
            }

            override fun onFailure(call: Call<VersionResponse>, t: Throwable) {
                versionRes.value = null
            }

        })
        return versionRes
    }
}