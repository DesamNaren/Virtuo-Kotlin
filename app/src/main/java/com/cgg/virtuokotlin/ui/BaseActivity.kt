package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.databinding.ActivityLoginBinding

open class BaseActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    lateinit var preferencesEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        preferences = VirtuoApplication.SharedPrefEditorObj.getPreferences()!!
        preferencesEditor = VirtuoApplication.SharedPrefEditorObj.getPreferencesEditor()!!
        Utils.changeStatusBarColor(
            context as Activity,
            VirtuoApplication.SharedPrefEditorObj.getPreferences()!!
        )
    }
}