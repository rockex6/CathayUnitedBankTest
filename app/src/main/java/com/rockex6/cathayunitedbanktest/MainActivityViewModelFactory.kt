package com.rockex6.cathayunitedbanktest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rockex6.cathayunitedbanktest.network.apiservices.StockServices
import com.rockex6.cathayunitedbanktest.network.util.RetrofitUtil

class MainActivityViewModelFactory(
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(providerMainActivityRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun providerMainActivityRepository(): MainActivityRepository {
        return MainActivityRepository(RetrofitUtil.getRetrofit().create(StockServices::class.java))
    }
}