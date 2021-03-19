package com.cgg.virtuokotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
    private val REQUEST_PERMISSION_CODE = 2000

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

    override fun onPermissionRequest(granted: Boolean) {
        if (!granted) {
            val customBinding = DataBindingUtil.setContentView<CustomLayoutForPermissionsBinding>(
                context as Activity,
                R.layout.custom_layout_for_permissions
            )
            customBinding.accept.setOnClickListener(View.OnClickListener {
                callPermissions()
            })
        }
    }

    private fun callPermissions() {
        ActivityCompat.requestPermissions(
            this@SplashActivity, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            ),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val mPIN = preferences.getString(AppConstants.MPIN, "");
                        when {
//                            TextUtils.isEmpty(mPIN) -> {
//                                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
//                            }
//                            else -> {
//                                startActivity(
//                                    Intent(
//                                        this@SplashActivity,
//                                        ValidateMPINActivity::class.java
//                                    )
//                                )
//                            }
                        }
                        finish()
                    }, 1_000)
                } else {
//                    customAlert()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}

/** Get Firebase Token*/
/*private fun Context.getFirebaseInstanceID() {
FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
Log.e("FCM_TOKEN", it.result!!.token)
preferencesEditor.putString(AppConstants.FCM_TOKEN, it.result!!.token)
preferencesEditor.commit()
}
}*/

/** Toast Display*/
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

