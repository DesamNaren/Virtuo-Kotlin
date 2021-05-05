package com.cgg.virtuokotlin.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.databinding.ActivityAutoOtpBinding
import com.cgg.virtuokotlin.interfaces.OtpInterface
import com.cgg.virtuokotlin.source.LoginReq
import com.cgg.virtuokotlin.source.LoginResponse
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
        binding.apply {
            firstPinView.requestFocus()
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(firstPinView, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun getSharedPrefData() {
        preferences.apply {
            userName = getString(AppConstants.USER_NAME, "")!!
            password = getString(AppConstants.PASSWORD, "")!!
            encUserName = getString(AppConstants.ENC_USER_NAME, "")!!
            encPwd = getString(AppConstants.ENC_USER_PWD, "")!!
            encMobileNum = getString(AppConstants.ENC_USER_MOB, "")!!
            fcmToken = getString(AppConstants.FCM_TOKEN, "")!!
            empName = getString(AppConstants.EMP_NAME, "")!!
            email = getString(AppConstants.USER_EMAIL, "")!!
            pic = getString(AppConstants.USER_PIC, "")!!
            data = getString(AppConstants.LOGIN_RESPONSE, "")!!
            loginResponse = Gson().fromJson(data, LoginResponse::class.java)
            loginResponse.data.apply {
                when {
                    !TextUtils.isEmpty(otpMobile) && !TextUtils.isEmpty(
                        mobileNumber
                    ) -> {
                        set6DigitText(mobileNumber)
                        //startSMSListener()
                    }
                    else -> {
                        toast(getString(R.string.something))
                    }
                }
            }

            binding.tvResend.setOnClickListener(View.OnClickListener {
                resendCall()
            })
        }
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
//        val viewModel: LoginViewModel =
//            ViewModelProvider(this).get(LoginViewModel::class.java)
//        viewModel.getLoginResponse(loginRequest)!!.observe(this@AutoOTPActivity, { loginResponse ->
//            this@AutoOTPActivity.loginResponse = loginResponse
//            if (loginResponse?.status_Code != null) {
//                if (loginResponse.status_Code == AppConstants.SESSION_EXPIRE) {
//                    toast("Session  Expired")
//                } else if (loginResponse.status_Code == AppConstants.SUCCESS_CODE) {
//                    otpTimer()
//                    storeLoginRes(loginResponse)
//                } else if (loginResponse.status_Code == AppConstants.FAILURE_CODE) {
//                    toast(loginResponse.status_Message)
//                } else {
//                    toast(getString(R.string.something))
//                }
//            } else {
//                toast("Server not responding")
//            }
//        })
    }


    private fun otpTimer() {
        val intervalObservable = Observable
            .interval(1000L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .takeWhile { aLong ->
                // stop the process if more than 30 seconds passes
                aLong < 30
            }
            .observeOn(AndroidSchedulers.mainThread())

        intervalObservable.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(aLong: Long) {
                if (cnt < 3) {
                    binding.tvResend.isEnabled = true
                    binding.tvResend.text =
                        getString(R.string.resend_otp) + " in" + aLong + " seconds"
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
        preferencesEditor.apply {
            val loginRes: String = Gson().toJson(loginResponse)
            putString(AppConstants.LOGIN_RESPONSE, loginRes)
            commit()
            set6DigitText(loginResponse.data.mobileNumber)
            toast(loginResponse.status_Message)
        }
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
        binding.firstPinView.apply {
            if (TextUtils.isEmpty(text)) {
                error = context.getString(R.string.please_enter_6_digit_otp)
                requestFocus()
            } else if (Objects.requireNonNull(text).toString().length < 6) {
                error = context.getString(R.string.please_enter_6_digit_otp)
                requestFocus()
            } else {
                if (!TextUtils.isEmpty(
                        loginResponse.data.otpMobile
                    )
                ) {
                    VerifyOTP(
                        text.toString(),
                        loginResponse.data.otpMobile
                    )
                } else {
                    toast("OTP is empty from server")
                }
            }
        }

    }

    private fun VerifyOTP(otp: String, actOtp: String) {
        binding.apply {
            Utils.hideKeyboard(context, btnSubmit)
            if (otp == actOtp) {
                firstPinView.error = null
                context.startActivity(Intent(context, AutoOTPActivity::class.java))
            } else {
                toast("Invalid OTP Entered")
                firstPinView.requestFocus()
            }
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