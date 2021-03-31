package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.AesEncryption
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
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
                loginRequest = LoginReq(
                    null,
                   null, "8790293290",
                    Utils.getDeviceID(context)!!,   Utils.getDeviceID(context)!!,   Utils.getDeviceID(context)!!
                )

                if (loginRequest.mobileNumber != null) {
                    encMob = aesEncryption.encrypt(loginRequest.mobileNumber)
                    encMob = encMob.replace("\n", "")
                    loginRequest.mobileNumber = encMob
                } else {
                    encUserName = aesEncryption.encrypt(loginRequest.userName)
                    encUserName = encUserName.replace("\n", "")
                    encPassword = aesEncryption.encrypt(loginRequest.password)
                    encPassword = encPassword.replace("\n", "")
                    loginRequest.userName = encUserName
                    loginRequest.password = encPassword
                }

                viewModel.getLoginResponse(
                    loginRequest
                )!!
                    .observe(this, Observer { loginResponse ->
                        when {
                            !TextUtils.isEmpty(loginResponse.data.otpMobile) && loginResponse.data.mpin == "00" -> {
                                navigateActivity(loginResponse, AutoOTPActivity::class.java)
                            }
                            else -> {
                                navigateActivity(loginResponse, AutoOTPActivity::class.java)
                            }
                        }
                    })
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    getString(R.string.something) + " in encryption",
                    Toast.LENGTH_SHORT
                ).show()
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
        preferencesEditor.putString(AppConstants.LOGIN_RESPONSE, loginRes)
        preferencesEditor.putString(AppConstants.USER_ID, loginResponse.data.userId.toString())
        preferencesEditor.putString(AppConstants.MOBILE_NUM, loginResponse.data.mobileNumber)
        preferencesEditor.putString(AppConstants.PASSWORD, loginRequest.password)
        preferencesEditor.putString(AppConstants.USER_NAME, loginResponse.data.userName)
        preferencesEditor.putString(AppConstants.USER_EMAIL, loginResponse.data.emailid)
        preferencesEditor.putString(AppConstants.USER_PIC, loginResponse.data.photopath)
        preferencesEditor.putString(AppConstants.EMP_NAME, loginResponse.data.employeeName)
        preferencesEditor.putString(AppConstants.SESSION_TOKEN, loginResponse.data.token)
        preferencesEditor.putString(AppConstants.ENC_USER_NAME, "encUserName")
        preferencesEditor.putString(AppConstants.ENC_USER_PWD, "encPassword")
        preferencesEditor.putString(AppConstants.ENC_USER_MOB, "encMob")
        if (!TextUtils.isEmpty(loginResponse.data.mpin) && loginResponse.data.mpin.length == 4)
            preferencesEditor.putString(AppConstants.MPIN, loginResponse.data.mpin)
        preferencesEditor.commit()

    }
}