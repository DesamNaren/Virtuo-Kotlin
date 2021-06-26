package com.cgg.virtuokotlin.application

import android.app.Application
import android.content.SharedPreferences
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.di.AppComponent
import com.cgg.virtuokotlin.di.DaggerAppComponent
import com.google.gson.Gson

class VirtuoApplication : Application() {
    companion object {
        lateinit var instance: VirtuoApplication
    }


    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    object SharedPrefEditorObj {
        fun getPreferences(): SharedPreferences? {
            return instance.getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE);
        }

        fun getPreferencesEditor(): SharedPreferences.Editor? {
            return getPreferences()!!.edit();
        }

        fun getGson(): Gson {
            return Gson()
        }
    }
}