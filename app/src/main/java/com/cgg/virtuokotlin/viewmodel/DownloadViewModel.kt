package com.cgg.virtuokotlin.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cgg.virtuokotlin.repository.DownloadRepository
import com.cgg.virtuokotlin.source.CoOrdinates
import com.cgg.virtuokotlin.source.CoOrdinatesResponse
import retrofit2.Response

class DownloadViewModel : ViewModel() {

    fun getData(token: String): LiveData<Response<CoOrdinatesResponse>> = liveData {
        emit(DownloadRepository().callCoOrdinates(token))
    }

    fun getDataLocal(context: Context, data: List<CoOrdinates>): LiveData<List<CoOrdinates>> =
        liveData {
            emit(DownloadRepository().insertCoOrdinates(context, data))
        }

}