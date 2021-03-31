package com.cgg.virtuokotlin.source

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val status_Message: String,
    val status_Code: Int,
    val data: LoginData
)

data class LoginData(
    val employeeId: String,
    val postId: String,
    val employeeName: String, val designation: String,
    val mobileNumber: String, val gender: String,
    val otpMobile: String, val name: String,
    val userId: Long, val userName: String,
    @SerializedName("photoPath")
    @Expose
    val photopath: String, val bloodGroup: String,
    val location: String, val mpin: String,
    val token: String, val refEmpID: String,
    @SerializedName("emailId")
    @Expose
    val emailid: String
)
