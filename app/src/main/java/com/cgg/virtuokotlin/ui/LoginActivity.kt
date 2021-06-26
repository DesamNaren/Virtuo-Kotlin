package com.cgg.virtuokotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.AesEncryption
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Status
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.databinding.ActivityLoginBinding
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.viewmodel.Factory
import com.cgg.virtuokotlin.viewmodel.LoginViewModel
import com.google.gson.Gson
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    private lateinit var encUserName: String
    private lateinit var encPassword: String
    private lateinit var encMob: String
    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context
    private lateinit var loginRequest: LoginReq


    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as VirtuoApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val aesEncryption: AesEncryption = AesEncryption.getInstance()

        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            try {
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
                        when {
                            mobileNumber != null -> {
                                encMob = encrypt(mobileNumber)
                                encMob = encMob.replace("\n", "")
                                mobileNumber = encMob
                            }
                            else -> {
                                encUserName = encrypt(userName)
                                encUserName = encUserName.replace("\n", "")
                                encPassword = encrypt(password)
                                encPassword = encPassword.replace("\n", "")
                                userName = encUserName
                                password = encPassword
                            }
                        }

                        viewModel.callLoginAPI(this)
                            .observe(this@LoginActivity, { response ->
                                response.let { resource ->
                                    when (resource.status) {
                                        Status.SUCCESS -> navigateActivity(
                                            resource.data!!.body()!!,
                                            DownloadActivity::class.java
                                        )
                                        Status.ERROR -> toast(response.message!!)
                                        Status.LOADING -> toast("Loading....")
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