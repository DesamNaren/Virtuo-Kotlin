package com.cgg.virtuokotlin.Utilities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.view.WindowManager
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
    }


}