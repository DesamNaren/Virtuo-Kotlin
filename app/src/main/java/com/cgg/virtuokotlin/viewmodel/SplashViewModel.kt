package com.cgg.virtuokotlin.viewmodel

import androidx.lifecycle.*
import com.cgg.virtuokotlin.Resource
import com.cgg.virtuokotlin.repository.SplashRepository
import com.cgg.virtuokotlin.source.VersionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: SplashRepository) : ViewModel() {

    private var liveData = MutableLiveData<Response<VersionResponse>>()

    fun callVersionAPI() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.callVersionAPI()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun callVersionAPI1() =
        viewModelScope.launch {
            liveData.postValue(repository.callVersionAPI())
        }

    fun getData(): LiveData<Response<VersionResponse>> {
        return liveData
    }
}