package com.cgg.virtuokotlin.di

import com.cgg.virtuokotlin.BuildConfig
import com.cgg.virtuokotlin.network.ViruoNetwork
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.SERVER_URL)
            .build()
    }


    @Singleton
    @Provides
    fun getNetWork(): ViruoNetwork {
        return provideRetrofit().create(ViruoNetwork::class.java)
    }
}