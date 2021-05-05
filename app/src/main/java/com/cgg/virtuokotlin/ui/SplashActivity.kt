package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Resource
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding
import com.cgg.virtuokotlin.interfaces.PermissionsCallback
import com.cgg.virtuokotlin.source.VersionData
import com.cgg.virtuokotlin.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SplashActivity : BaseActivity(), PermissionsCallback {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var context: Context
    private lateinit var mPIN: String
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        mPIN = preferences.getString(AppConstants.MPIN, "")!!
        Log.i(Companion.TAG, "onCreate")
        /**Setting Theme Background*/

        binding.root.background.apply {
            var themeColor = preferences.getInt(AppConstants.THEME_COLOR, -1);
                when (themeColor) {
                    -1 -> {
                        binding.root.background = ContextCompat.getDrawable(context, R.drawable.splash1)
                        themeColor = R.drawable.theme_one
                        preferencesEditor.putInt(AppConstants.THEME_COLOR, themeColor)
                        preferencesEditor.commit()
                    }
                    R.drawable.theme_one ->
                        ContextCompat.getDrawable(context, R.drawable.splash1)
                    R.drawable.theme_two ->
                        ContextCompat.getDrawable(context, R.drawable.splash2)
                    R.drawable.theme_three ->
                        ContextCompat.getDrawable(context, R.drawable.splash3)
                    R.drawable.theme_four ->
                        ContextCompat.getDrawable(context, R.drawable.splash4)
                    R.drawable.theme_five ->
                        ContextCompat.getDrawable(context, R.drawable.splash5)
                }
        }

        /** Call Version API*/
        runBlocking {
            Log.i(Companion.TAG, "onCreate: runBlocking")
            delay(2000)
            callVersionCheck()
        }
    }


    private fun callVersionCheck() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        viewModel.apply {
            get().observe(this@SplashActivity, {
                if (it!!.isSuccessful) {
                    val versionData: VersionData = it.body()!!.data
                    toast(versionData.version_no)
                    navigateActivity(mPIN)
                }
            })
        }
    }

    private fun navigateActivity(mPIN: String) {
        when {
            TextUtils.isEmpty(mPIN) -> {
                callActivity(LoginActivity::class.java)
            }
            else -> {
                callActivity(LoginActivity::class.java) // ValidateMPIN
            }
        }
        finish()
    }

    private fun callActivity(activity: Class<out Activity?>) {
        startActivity(Intent(this, activity))
    }

    override fun onPermissionCallBack(granted: Boolean) {
        when {
            granted -> {
                navigateActivity(mPIN)
            }
            else -> {
                navigateActivity(mPIN)
            }
        }
    }

    companion object {
        private const val TAG = "SplashActivity"
    }
}







