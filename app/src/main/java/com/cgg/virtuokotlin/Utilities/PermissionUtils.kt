package com.cgg.virtuokotlin.Utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import com.cgg.virtuokotlin.interfaces.PermissionsCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionUtils : MultiplePermissionsListener {
    private lateinit var callback: PermissionsCallback
    private val permissions =
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)

    fun requestPermissions(context: Context, callback: PermissionsCallback) {
        this.callback = callback
        Dexter.withActivity(context as Activity)
            .withPermissions(permissions)
            .withListener(this)
            .check()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        return when {
            report!!.areAllPermissionsGranted() ->
                callback.onPermissionRequest(true)

            report.isAnyPermissionPermanentlyDenied ->
                callback.onPermissionRequest(false)

            else ->
                callback.onPermissionRequest(false)

        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest()
    }
}