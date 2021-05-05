package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.AesEncryption
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.databinding.ActivityLoginBinding
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.viewmodel.LoginViewModel
import com.google.gson.Gson

class LoginActivity : BaseActivity() {
    private lateinit var encUserName: String
    private lateinit var encPassword: String
    private lateinit var encMob: String
    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context
    private lateinit var loginRequest: LoginReq

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            val viewModel: LoginViewModel =
                ViewModelProvider(this).get(LoginViewModel::class.java)

            try {
                val aesEncryption: AesEncryption = AesEncryption.getInstance()
                aesEncryption.apply {
                    loginRequest = LoginReq(
                        null,
                        null,
                        "8790293290",
                        Utils.getDeviceID(context)!!,
                        Utils.getDeviceID(context)!!,
                        Utils.getDeviceID(context)!!
                    )

                    loginRequest.apply {
                        if (mobileNumber != null) {
                            encMob = encrypt(mobileNumber)
                            encMob = encMob.replace("\n", "")
                            mobileNumber = encMob
                        } else {
                            encUserName = encrypt(userName)
                            encUserName = encUserName.replace("\n", "")
                            encPassword = encrypt(password)
                            encPassword = encPassword.replace("\n", "")
                            userName = encUserName
                            password = encPassword
                        }

                        viewModel.get(
                            this
                        ).observe(this@LoginActivity, Observer { loginResponse ->
                            loginResponse.body()!!.apply {
                                when {
                                    !TextUtils.isEmpty(data.otpMobile) &&
                                            data.mpin == "00" -> {
                                        navigateActivity(
                                            this,
                                            DownloadActivity::class.java //AutoOtpActivity
                                        )
                                    }
                                    else -> {

                                        navigateActivity(
                                            this,
                                            DownloadActivity::class.java
                                        )
                                    }
                                }
                            }
                        })
                    }


                }


            } catch (e: Exception) {
                toast(getString(R.string.something) + " in encryption")
                e.printStackTrace()
            }
        })
    }


    private fun navigateActivity(loginRes: LoginResponse, activity: Class<out Activity?>) {
        storeLoginRes(loginRes)
        startActivity(Intent(context, activity))
        finish()
    }

    private fun storeLoginRes(loginResponse: LoginResponse) {
        val gSon = Gson()
        val loginRes = gSon.toJson(loginResponse)
        preferencesEditor.apply {
            putString(AppConstants.LOGIN_RESPONSE, loginRes)

            putString(AppConstants.PASSWORD, loginRequest.password)

            loginResponse.data.apply {
                putString(AppConstants.MOBILE_NUM, mobileNumber)
                putString(AppConstants.USER_ID, userId.toString())
                putString(AppConstants.USER_NAME, userName)
                putString(AppConstants.USER_EMAIL, emailid)
                putString(AppConstants.USER_PIC, photopath)
                putString(AppConstants.EMP_NAME, employeeName)
                putString(AppConstants.SESSION_TOKEN, token)
                if (!TextUtils.isEmpty(mpin) && mpin.length == 4)
                    putString(AppConstants.MPIN, mpin)
            }

            putString(AppConstants.ENC_USER_NAME, "encUserName")
            putString(AppConstants.ENC_USER_PWD, "encPassword")
            putString(AppConstants.ENC_USER_MOB, "encMob")
            commit()
        }
    }
}