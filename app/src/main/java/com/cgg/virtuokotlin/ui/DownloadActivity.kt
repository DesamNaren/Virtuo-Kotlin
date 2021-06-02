package com.cgg.virtuokotlin.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Status
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Extensions.toast
import com.cgg.virtuokotlin.databinding.ActivityDownloadBinding
import com.cgg.virtuokotlin.source.CoOrdinates
import com.cgg.virtuokotlin.viewmodel.DownloadViewModel
import com.cgg.virtuokotlin.viewmodel.Factory

class DownloadActivity : BaseActivity() {
    private lateinit var viewModel: DownloadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDownloadBinding>(
            this,
            R.layout.activity_download
        )
        val token = preferences.getString(AppConstants.SESSION_TOKEN, "")

        binding.data = CoOrdinates(100, "", "", "", "", "", false)

        viewModel = ViewModelProvider(this, Factory()).get(DownloadViewModel::class.java)
        token?.let {
            viewModel.callCoOrdinates(it).observe(this, { response ->
                response?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            insertCoOrdinates(response.data!!.body()!!.data)
                        }
                        Status.ERROR -> {
                            toast(response.message.toString())
                        }
                        Status.LOADING -> {
                            toast("Loading....")
                        }
                    }
                }
            })
        }
    }

    private fun insertCoOrdinates(data: List<CoOrdinates>) {
        viewModel.insertCoOrdinates(this, data)
            .observe(this) { localRes ->
                toast("Inserted ${localRes.size}")
            }
    }
}