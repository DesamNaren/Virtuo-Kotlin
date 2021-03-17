package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
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
        callVersionCheck()

    }
}

/*    private fun Context.getFirebaseInstanceID() {
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
            Log.e("FCM_TOKEN", it.result!!.token)
            preferencesEditor.putString(AppConstants.FCM_TOKEN, it.result!!.token)
            preferencesEditor.commit()
        }
    }*/

private fun Context.callVersionCheck() {
    when (Utils.checkInternetConnection(this)) {
        false -> toast("No Internet Connection")
    }
    Utils.checkInternetConnection(this)
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
