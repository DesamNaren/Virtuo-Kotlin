package com.cgg.virtuokotlin.source

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CoOrdinatesResponse(
    val success: String,
    val status_message: String,
    val status_Code: Int,
    val data: List<CoOrdinates>
)

@Entity(tableName = "Location_Info")
data class CoOrdinates(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val address: String,
    val latitude: String,
    val location: String,
    @ColumnInfo(name = "radius") val allowableradius: String,
    val longitude: String,
    val isActive: Boolean
): BaseObservable()
