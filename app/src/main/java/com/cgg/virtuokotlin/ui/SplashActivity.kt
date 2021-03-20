package com.cgg.virtuokotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.PermissionUtils
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding
import com.cgg.virtuokotlin.databinding.CustomLayoutForPermissionsBinding
import com.cgg.virtuokotlin.interfaces.PermissionsCallback
import com.cgg.virtuokotlin.viewmodel.SplashViewModel


class SplashActivity : BaseActivity(), PermissionsCallback {
    companion object {
        private const val REQUEST_PERMISSION_CODE: Int = 2000
    }

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private lateinit var viewModel: SplashViewModel
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@SplashActivity
        binding = DataBindingUtil.setContentView(context as Activity, R.layout.activity_splash)
        preferences = VirtuoApplication.SharedPrefEditorObj.getPreferences()!!
        preferencesEditor = VirtuoApplication.SharedPrefEditorObj.getPreferencesEditor()!!

        /**Setting Screen Background*/
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
        /*getFirebaseInstanceID()*/

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        callVersionCheck()
    }

    /** Call Version API*/
    private fun callVersionCheck() {
        when (Utils.checkInternetConnection(this)) {
            false -> toast("No Internet Connection")
            else -> {
                viewModel.getVersionResponse()!!.observe(this@SplashActivity, { versionResponse ->
                    if (versionResponse.data.version_no == Utils.getVersionName(context)) {
                        AppConstants.VERSION_DATE = versionResponse.data.lastupdated_date

                        PermissionUtils.requestPermissions(context, this)

                    } else toast("Update Latest Version")
                })
            }
        }
    }

    /** Request Permissions*/
    private fun callPermissions() {
        ActivityCompat.requestPermissions(
            this@SplashActivity, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            ),
            Companion.REQUEST_PERMISSION_CODE
        )
    }

    /** Request Permissions Results*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                val mPIN: String = preferences.getString(AppConstants.MPIN, "")!!
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    navigateActivity(mPIN)
                } else {
                    navigateActivity(mPIN)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun navigateActivity(mPIN: String) {
        when {
            TextUtils.isEmpty(mPIN) -> {
                callActivity(SplashActivity::class.java)
            }
            else -> {
                callActivity(SplashActivity::class.java)
            }
        }
        finish()
    }

    private fun callActivity(activity: Class<out Activity?>) {
        startActivity(Intent(this, activity::class.java))
    }

    override fun onPermissionRequest(granted: Boolean) {
        if (!granted) {
            val customBinding = DataBindingUtil.setContentView<CustomLayoutForPermissionsBinding>(
                context as Activity,
                R.layout.custom_layout_for_permissions
            )
            customBinding.accept.setOnClickListener {
                callPermissions()
            }
        }
    }
}

/** Toast Display*/
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/** Get Firebase Token*/
/*private fun Context.getFirebaseInstanceID() {
FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
Log.e("FCM_TOKEN", it.result!!.token)
preferencesEditor.putString(AppConstants.FCM_TOKEN, it.result!!.token)
preferencesEditor.commit()
}
}*/







