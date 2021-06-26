package com.cgg.virtuokotlin.di

import android.content.Context
import com.cgg.virtuokotlin.repository.SplashRepository
import com.cgg.virtuokotlin.ui.DownloadActivity
import com.cgg.virtuokotlin.ui.LoginActivity
import com.cgg.virtuokotlin.ui.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(splashActivity: SplashActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(downloadActivity: DownloadActivity)
}