package com.cgg.virtuokotlin.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.application.VirtuoApplication

private const val TAG = "VirtuoService"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VirtuoApplication.SharedPrefEditorObj.getPreferencesEditor()
        VirtuoApplication.SharedPrefEditorObj.getPreferencesEditor()
        VirtuoApplication.SharedPrefEditorObj.getGson()
        VirtuoApplication.SharedPrefEditorObj.getGson()


        val context: Context = VirtuoApplication.instance
        Log.i(TAG, context.hashCode().toString())
        Log.i(TAG, context.hashCode().toString())
//        VirtuoService.RetrofitInstance.createInstance().getVersionCheck()
//        VirtuoService.RetrofitInstance.createInstance().getVersionCheck()
//        VirtuoService.RetrofitInstance.createInstance().getVersionCheck()
    }
}