package com.cgg.virtuokotlin.source

data class LoginReq(
    var userName: String?,
    var password: String?,
    var mobileNumber: String?,
    val deviceId: String,
    val IMEI: String,
    val fcmtoken: String,
)
