package com.cgg.virtuokotlin.source

data class VersionResponse(
    val success: Boolean,
    val status_Message: String,
    val status_Code: Int,
    val data: VersionData
)

data class VersionData(
    val version_no: String, val lastupdated_date: String
)