package com.cgg.virtuokotlin.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.databinding.ActivityAutoOtpBinding
import com.cgg.virtuokotlin.interfaces.OtpInterface
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
import com.cgg.virtuokotlin.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class AutoOTPActivity : BaseActivity(), OtpInterface {
    private var cnt: Int = 0
    private lateinit var binding: ActivityAutoOtpBinding
    private lateinit var context: Context
    private lateinit var pic: String
    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var fcmToken: String
    private lateinit var encUserName: String
    private lateinit var encPwd: String
    private lateinit var encMobileNum: String
    private lateinit var empName: String
    private lateinit var email: String
    private lateinit var data: String
    private lateinit var devID: String
    private lateinit var loginResponse: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_otp)
        devID = Utils.getDeviceID(context)!!
        setInitialFocus()
        getSharedPrefData();
        loadPic()
        textChange()

        binding.btnSubmit.setOnClickListener { callSubmit() }
    }

    private fun setInitialFocus() {
        binding.firstPinView.requestFocus()
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(binding.firstPinView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getSharedPrefData() {
        userName = preferences.getString(AppConstants.USER_NAME, "")!!
        password = preferences.getString(AppConstants.PASSWORD, "")!!
        encUserName = preferences.getString(AppConstants.ENC_USER_NAME, "")!!
        encPwd = preferences.getString(AppConstants.ENC_USER_PWD, "")!!
        encMobileNum = preferences.getString(AppConstants.ENC_USER_MOB, "")!!
        fcmToken = preferences.getString(AppConstants.FCM_TOKEN, "")!!
        empName = preferences.getString(AppConstants.EMP_NAME, "")!!
        email = preferences.getString(AppConstants.USER_EMAIL, "")!!
        pic = preferences.getString(AppConstants.USER_PIC, "")!!
        data = preferences.getString(AppConstants.LOGIN_RESPONSE, "")!!
        loginResponse = Gson().fromJson(data, LoginResponse::class.java)
        when {
            !TextUtils.isEmpty(loginResponse.data.otpMobile) && !TextUtils.isEmpty(
                loginResponse.data.mobileNumber
            ) -> {
                set6DigitText(loginResponse.data.mobileNumber)
                //startSMSListener()
            }
            else -> {
                toast(getString(R.string.something))
            }
        }
        binding.tvResend.setOnClickListener(View.OnClickListener {
            resendCall()
        })

    }

    private fun loadPic() {
        Glide.with(this)
            .load(pic)
            .error(R.drawable.virtuo_circle_logo)
            .placeholder(R.drawable.virtuo_circle_logo)
            .into(binding.profileImage)
    }

    private fun textChange() {
        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString())) {
                    val otp = s.toString()
                    if (otp.length == 6) {
                        Utils.hideKeyboard(context, binding.btnSubmit)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun set6DigitText(mobileNumber: String) {
        val maskedMobNum = mobileNumber.replace("\\w(?=\\w{4})".toRegex(), "*")
        binding.enter6DigitOtp.text = getString(R.string.enter_6_digit_otp) + " $maskedMobNum"
    }

    private fun resendCall() {
        //startSMSListener()
        cnt++
        binding.firstPinView.setText("")
        val loginRequest =
            LoginReq(
                encUserName,
                encPwd, encMobileNum,
                devID, devID, fcmToken
            )

        if (TextUtils.isEmpty(password)) {
            loginRequest.userName = null
            loginRequest.password = null
        } else {
            loginRequest.mobileNumber = null
        }
        val viewModel: LoginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.getLoginResponse(loginRequest)!!.observe(this@AutoOTPActivity, { loginResponse ->
            this@AutoOTPActivity.loginResponse = loginResponse
            if (loginResponse?.status_Code != null) {
                if (loginResponse.status_Code == AppConstants.SESSION_EXPIRE) {
                    toast("Session  Expired")
                } else if (loginResponse.status_Code == AppConstants.SUCCESS_CODE) {
                    otpTimer()
                    storeLoginRes(loginResponse)
                } else if (loginResponse.status_Code == AppConstants.FAILURE_CODE) {
                    toast(loginResponse.status_Message)
                } else {
                    toast(getString(R.string.something))
                }
            } else {
                toast("Server not responding")
            }
        })
    }


    private fun otpTimer() {

        val intervalObservable = Observable
            .interval(1000L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .takeWhile(object : Predicate<Long?> {
                // stop the process if more than 5 seconds passes
                @Throws(Exception::class)
                override fun test(aLong: Long): Boolean {
                    return aLong < 30
                }
            })
            .observeOn(AndroidSchedulers.mainThread())

        intervalObservable.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(aLong: Long) {
                if (cnt < 3) {
                    binding.tvResend.isEnabled = true
                    binding.tvResend.text = getString(R.string.resend_otp)+" in"+aLong+" seconds"
                    //startSMSListener()
                } else {
                    binding.tvResend.visibility = View.GONE
                }
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })

//        object : CountDownTimer(30000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.tvResend.isEnabled = false
//                binding.tvResend.text =
//                    getString(R.string.resend_otp) + ": " + millisUntilFinished / 1000 + " Seconds"
//                //here you can have your logic to set text to edittext
//            }
//
//            override fun onFinish() {
//                if (cnt < 3) {
//                    binding.tvResend.isEnabled = true
//                    binding.tvResend.text = getString(R.string.resend_otp)
//                    //startSMSListener()
//                } else {
//                    binding.tvResend.visibility = View.GONE
//                }
//            }
//        }.start()
    }

    private fun storeLoginRes(loginResponse: LoginResponse) {
        val loginRes: String = Gson().toJson(loginResponse)
        preferencesEditor.putString(AppConstants.LOGIN_RESPONSE, loginRes)
        preferencesEditor.commit()
        set6DigitText(loginResponse.data.mobileNumber)
        toast(loginResponse.status_Message)
    }

    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(this)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener {
            //call server to get sms by sending mobile number
        }
        mTask.addOnFailureListener {
            Toast.makeText(
                this@AutoOTPActivity,
                "Error",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callSubmit() {
        if (TextUtils.isEmpty(binding.firstPinView.text)) {
            binding.firstPinView.error = context.getString(R.string.please_enter_6_digit_otp)
            binding.firstPinView.requestFocus()
        } else if (Objects.requireNonNull(binding.firstPinView.text).toString().length < 6) {
            binding.firstPinView.error = context.getString(R.string.please_enter_6_digit_otp)
            binding.firstPinView.requestFocus()
        } else {
            if (!TextUtils.isEmpty(
                    loginResponse.data.otpMobile
                )
            ) {
                VerifyOTP(
                    binding.firstPinView.text.toString(),
                    loginResponse.data.otpMobile
                )
            } else {
                toast("OTP is empty from server")
            }
        }
    }

    private fun VerifyOTP(otp: String, actOtp: String) {
        Utils.hideKeyboard(context, binding.btnSubmit)
        if (otp == actOtp) {
            binding.firstPinView.error = null
            context.startActivity(Intent(context, AutoOTPActivity::class.java))
        } else {
            toast("Invalid OTP Entered")
            binding.firstPinView.requestFocus()
        }
    }

    override fun onOtpReceived(otp: String) {
        binding.firstPinView.setText(otp)
        callSubmit()
    }

    override fun onOtpTimeOut() {
        //startSMSListener()
        binding.tvResend.visibility = View.VISIBLE
        toast("Time out, please resend")
    }
}