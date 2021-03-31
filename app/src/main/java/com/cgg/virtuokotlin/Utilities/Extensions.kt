package com.cgg.virtuokotlin.Utilities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.databinding.ActivityLoginBinding
import com.cgg.virtuokotlin.databinding.ActivitySplashBinding
import com.google.firebase.installations.FirebaseInstallations

object Extensions {
    /** Show Toast*/
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**Setting Theme Background*/
    fun Activity.test(sharedPreferences: SharedPreferences?, binding: ActivitySplashBinding?) {
        val selectedThemeColor = sharedPreferences!!.getInt("theme_color", -1)
        when {
            selectedThemeColor != -1 ->
                binding!!.root.setBackgroundResource(selectedThemeColor)

            else ->
                binding!!.root.setBackgroundResource(R.drawable.theme_four)

        }
        Utils.changeStatusBarColor(this, sharedPreferences)

    }

    /**Setting Login Theme Background*/
    fun Activity.loginBg(sharedPreferences: SharedPreferences?, binding: ActivityLoginBinding?) {
        val selectedThemeColor = sharedPreferences!!.getInt("theme_color", -1)
        when {
            selectedThemeColor != -1 ->
                binding!!.root.setBackgroundResource(selectedThemeColor)

            else ->
                binding!!.root.setBackgroundResource(R.drawable.theme_four)

        }
        Utils.changeStatusBarColor(this, sharedPreferences)

    }
    /** Get Firebase Token*/

    fun Context.firebaseToken(preferencesEditor: SharedPreferences.Editor) {
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
            Log.e("FCM_TOKEN", it.result!!.token)
            preferencesEditor.putString(AppConstants.FCM_TOKEN, it.result!!.token)
            preferencesEditor.commit()
        }
    }

}


