package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Status
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding
import com.cgg.virtuokotlin.interfaces.PermissionsCallback
import com.cgg.virtuokotlin.source.VersionData
import com.cgg.virtuokotlin.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashActivity : BaseActivity(), PermissionsCallback {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var context: Context
    private lateinit var mPIN: String
    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as VirtuoApplication).appComponent.inject(this)

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
        viewModel.apply {
            callVersionAPI().observe(this@SplashActivity, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val versionData: VersionData = it.data!!.body()!!.data
                            toast(versionData.version_no)
                            navigateActivity()
                        }
                        Status.ERROR -> {
                            toast(it.message.toString())
                        }
                        Status.LOADING -> {
                            toast("Loading....")
                        }
                    }
                }
            })
        }
    }

    private fun navigateActivity() {
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
                navigateActivity()
            }
            else -> {
                navigateActivity()
            }
        }
    }

    companion object {
        private const val TAG = "SplashActivity"
    }
}







