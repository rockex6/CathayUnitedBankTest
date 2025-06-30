package com.rockex6.cathayunitedbanktest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockex6.cathayunitedbanktest.model.StockBasicInfo
import com.rockex6.cathayunitedbanktest.model.StockDayAverage
import com.rockex6.cathayunitedbanktest.model.StockTrading
import com.rockex6.cathayunitedbanktest.network.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val mainActivityRepository: MainActivityRepository) :
    ViewModel() {

    private val _allStock = MutableStateFlow<List<StockBasicInfo>>(emptyList())
    val allStock = _allStock.asStateFlow()

    private val _allDayAverage = MutableStateFlow<List<StockDayAverage>>(emptyList())
    val allDayAverage = _allDayAverage.asStateFlow()

    private val _allDayStock = MutableStateFlow<List<StockTrading>>(emptyList())
    val allDayStock = _allDayStock.asStateFlow()

    fun getAllStock() {
        viewModelScope.launch(Dispatchers.IO) {
            mainActivityRepository.getAllStock().collect {
                when (it) {
                    is ApiResult.Success -> {
                        _allStock.value = it.data
                    }

                    is ApiResult.Failure -> {
                        // Handle failure
                    }
                }
            }
        }
    }

    fun getAllDayAverage() {
        viewModelScope.launch(Dispatchers.IO) {
            mainActivityRepository.getAllDayAverage().collect {
                when (it) {
                    is ApiResult.Success -> {
                        // Handle success
                    }

                    is ApiResult.Failure -> {
                        // Handle failure
                    }
                }
            }
        }
    }

    fun getAllDayStock() {
        viewModelScope.launch(Dispatchers.IO) {
            mainActivityRepository.getAllDayStock().collect {
                when (it) {
                    is ApiResult.Success -> {
                        _allDayStock.value = it.data
                    }

                    is ApiResult.Failure -> {
                        // Handle failure
                    }
                }
            }
        }
    }
}