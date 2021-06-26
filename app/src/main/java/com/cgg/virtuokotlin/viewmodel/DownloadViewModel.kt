package com.cgg.virtuokotlin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cgg.virtuokotlin.Resource
import com.cgg.virtuokotlin.repository.DownloadRepository
import com.cgg.virtuokotlin.source.CoOrdinates
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DownloadViewModel @Inject constructor(private val repository: DownloadRepository) : ViewModel() {

    fun callCoOrdinates(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(repository.callCoOrdinates(token)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertCoOrdinates(context: Context, data: List<CoOrdinates>) = liveData(Dispatchers.IO) {
        emit(repository.insertCoOrdinates(context, data))
    }
}