package com.cgg.virtuokotlin.repository

import android.content.Context
import com.cgg.virtuokotlin.db.VirtuoDatabase
import com.cgg.virtuokotlin.network.ViruoNetwork
import com.cgg.virtuokotlin.source.CoOrdinates
import com.cgg.virtuokotlin.source.CoOrdinatesResponse
import retrofit2.Response
import javax.inject.Inject

class DownloadRepository @Inject constructor(private val vService: ViruoNetwork) {

    suspend fun callCoOrdinates(token: String): Response<CoOrdinatesResponse> {
        return vService.getOfficeCoOrdinates(token)
    }

    suspend fun insertCoOrdinates(
        context: Context,
        data: List<CoOrdinates>
    ): List<CoOrdinates> {
        val db: VirtuoDatabase? = VirtuoDatabase.getDatabase(context)
        db!!.dao()!!.insertCoOrdinates(data)
        return db.dao()!!.getCoOrdinates()
    }
}