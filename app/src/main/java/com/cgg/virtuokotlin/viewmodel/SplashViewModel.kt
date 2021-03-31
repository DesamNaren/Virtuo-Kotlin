package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cgg.virtuokotlin.repository.SplashRepository
import com.cgg.virtuokotlin.source.VersionResponse

class SplashViewModel : ViewModel() {
    lateinit var versionRes: MutableLiveData<VersionResponse?>

    fun getVersionResponse(): LiveData<VersionResponse?> {
        versionRes = SplashRepository.callVersionCheckAPI()
        return versionRes
    }

}