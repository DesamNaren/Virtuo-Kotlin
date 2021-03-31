package com.cgg.virtuokotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.test
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.Utilities.PermissionUtils
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding
import com.cgg.virtuokotlin.databinding.CustomLayoutForPermissionsBinding
import com.cgg.virtuokotlin.interfaces.PermissionsCallback
import com.cgg.virtuokotlin.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity(), PermissionsCallback {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var context: Context
    private lateinit var mPIN: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        mPIN = preferences.getString(AppConstants.MPIN, "")!!

        /**Setting Theme Background*/

        var themeColor = preferences.getInt(AppConstants.THEME_COLOR, -1);
        when (themeColor) {
            -1 -> {
                binding.root.background = ContextCompat.getDrawable(context, R.drawable.splash1)
                themeColor = R.drawable.theme_one
                preferencesEditor.putInt(AppConstants.THEME_COLOR, themeColor)
                preferencesEditor.commit()
            }
            R.drawable.theme_one -> binding.root.background =
                ContextCompat.getDrawable(context, R.drawable.splash1)
            R.drawable.theme_two -> binding.root.background =
                ContextCompat.getDrawable(context, R.drawable.splash2)
            R.drawable.theme_three -> binding.root.background =
                ContextCompat.getDrawable(context, R.drawable.splash3)
            R.drawable.theme_four -> binding.root.background =
                ContextCompat.getDrawable(context, R.drawable.splash4)
            R.drawable.theme_five -> binding.root.background =
                ContextCompat.getDrawable(context, R.drawable.splash5)
        }

        /** Call Version API*/
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            callVersionCheck()
        }, 2000)

    }

    /** Call Version API*/
    private fun callVersionCheck() {
        when (Utils.checkInternetConnection(this)) {
            false -> toast("No Internet Connection")
            else -> {
                val viewModel: SplashViewModel =
                    ViewModelProvider(this).get(SplashViewModel::class.java)

                viewModel.getVersionResponse().observe(this@SplashActivity, { versionResponse ->
                    if (versionResponse!!.data.version_no == Utils.getVersionName(context)) {
                        if (preferences.getBoolean(AppConstants.PER_ASKED, false)) {
                            navigateActivity(mPIN)
                        } else {
                            AppConstants.VERSION_DATE = versionResponse.data.lastupdated_date
                            val permissionLoc = ContextCompat.checkSelfPermission(
                                this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            val permissionCam = ContextCompat.checkSelfPermission(
                                this@SplashActivity, Manifest.permission.CAMERA
                            )
                            when {
                                permissionLoc != PackageManager.PERMISSION_GRANTED || permissionCam != PackageManager.PERMISSION_GRANTED -> {
                                    val customBinding =
                                        DataBindingUtil.setContentView<CustomLayoutForPermissionsBinding>(
                                            this@SplashActivity,
                                            R.layout.custom_layout_for_permissions
                                        )
                                    customBinding.accept.setOnClickListener {
                                        preferencesEditor.putBoolean(AppConstants.PER_ASKED, true)
                                        preferencesEditor.commit()
                                        PermissionUtils.requestPermissions(context, this)
                                    }
                                }
                                else -> {
                                    navigateActivity(mPIN)
                                }
                            }
                        }

                    } else toast("Update Latest Version")
                })
            }
        }
    }

    private fun navigateActivity(mPIN: String) {
        when {
            TextUtils.isEmpty(mPIN) -> {
                callActivity(LoginActivity::class.java)
            }
            else -> {
                callActivity(SplashActivity::class.java)
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
}







