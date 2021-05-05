package com.cgg.virtuokotlin.ui

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.databinding.ActivityDownloadBinding
import com.cgg.virtuokotlin.source.CoOrdinates
import com.cgg.virtuokotlin.viewmodel.DownloadViewModel

class DownloadActivity : BaseActivity() {
    private val TAG = "DownloadActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDownloadBinding>(this, R.layout.activity_download)
        val token = preferences.getString(AppConstants.SESSION_TOKEN, "")

        binding.data= CoOrdinates(100, "","","","","",false)

        val viewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        token?.let {
            viewModel.getData(it).observe(this, { res ->
                if (res.isSuccessful) {
                    Log.i(TAG, "onCreate: ${res.body()!!.data.size}")
                    viewModel.getDataLocal(this, res.body()!!.data).observe(this, { localRes ->
                            Log.i(TAG, "onCreateLocal: ${localRes.size}")
                    })
                }
            })
        }
    }
}