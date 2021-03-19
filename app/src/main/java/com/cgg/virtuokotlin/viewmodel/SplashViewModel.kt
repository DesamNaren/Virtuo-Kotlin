package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cgg.virtuokotlin.repository.VersionRepository
import com.cgg.virtuokotlin.source.VersionResponse

class SplashViewModel : ViewModel() {
    var versionRes: MutableLiveData<VersionResponse>? = null

    fun getVersionResponse(): LiveData<VersionResponse>? {
        versionRes = VersionRepository.callVersionCheckAPI()
        return versionRes
    }
}