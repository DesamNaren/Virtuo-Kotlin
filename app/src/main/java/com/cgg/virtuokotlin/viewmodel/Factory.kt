package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.network.getNetworkService
import com.cgg.virtuokotlin.repository.DownloadRepository
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.repository.SplashRepository

class Factory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(SplashRepository(getNetworkService())) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(LoginRepository(getNetworkService())) as T
        } else if (modelClass.isAssignableFrom(DownloadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DownloadViewModel(DownloadRepository(getNetworkService())) as T
        }
        throw IllegalArgumentException("Unable to construct view model")
    }
}