package com.cgg.virtuokotlin.interfaces

interface OtpInterface {
    fun onOtpReceived(otp: String)
    fun onOtpTimeOut()
}