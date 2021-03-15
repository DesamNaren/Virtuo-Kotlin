package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        Utils.changeStatusBarColor(
            context as Activity,
            VirtuoApplication.SharedPrefEditorObj.getPreferences()!!
        )
    }
}