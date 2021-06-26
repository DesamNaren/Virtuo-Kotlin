package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.network.ViruoNetwork
import com.cgg.virtuokotlin.repository.DownloadRepository
import com.cgg.virtuokotlin.repository.LoginRepository
import com.cgg.virtuokotlin.repository.SplashRepository
import javax.inject.Inject

class Factory() : ViewModelProvider.Factory {


    @Inject
    lateinit var repository: SplashRepository

    @Inject
    lateinit var loginRepository: LoginRepository

    @Inject
    lateinit var downloadRepository: DownloadRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(loginRepository) as T
            }
            modelClass.isAssignableFrom(DownloadViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return DownloadViewModel(downloadRepository) as T
            }
        }
        throw IllegalArgumentException("Unable to construct view model")
    }
}