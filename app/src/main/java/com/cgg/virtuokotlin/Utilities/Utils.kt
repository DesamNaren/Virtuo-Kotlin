package com.cgg.virtuokotlin.Utilities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.cgg.virtuokotlin.R
import java.util.*

class Utils {
    companion object {
        fun changeStatusBarColor(activity: Activity, sharedPreferences: SharedPreferences) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val view = activity.window
                    view.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    view.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                    when (sharedPreferences.getInt(AppConstants.THEME_COLOR, -1)) {
                        -1 -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[0]
                        }
                        R.drawable.theme_one -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[0]
                        }
                        R.drawable.theme_two -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[1]
                        }
                        R.drawable.theme_three -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[2]
                        }
                        R.drawable.theme_four -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[3]
                        }
                        R.drawable.theme_five -> {
                            val colorsIds =
                                activity.resources.getIntArray(R.array.theme_colors_id)
                            view.statusBarColor = colorsIds[4]
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun generateRandomNum(): Int {
            return Random().nextInt(100000)
        }

        fun checkInternetConnection(context: Context): Boolean {
            var result = false
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

        fun getVersionName(context: Context): String {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        }

        fun getDeviceID(context: Context): String? {
            var deviceID: String? = ""
            try {
                deviceID = Settings.Secure.getString(
                    context.contentResolver, Settings.Secure.ANDROID_ID
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return deviceID
        }

        fun hideKeyboard(context: Context, mView: View) {
            try {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(mView.windowToken, 0)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}