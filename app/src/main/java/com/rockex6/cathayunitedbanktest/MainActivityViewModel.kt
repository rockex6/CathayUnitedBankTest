package com.rockex6.cathayunitedbanktest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockex6.cathayunitedbanktest.model.StockTrading
import com.rockex6.cathayunitedbanktest.network.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivityViewModel(private val mainActivityRepository: MainActivityRepository) :
    ViewModel() {

    private val _allStockList = MutableStateFlow<List<StockTrading>>(emptyList())
    val allStockList = _allStockList.asStateFlow()

    // 載入狀態管理
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun getAllStockData() = viewModelScope.launch(Dispatchers.IO) {
        // 開始載入，顯示 loading
        _isLoading.value = true

        try {
            val stockFlow = mainActivityRepository.getAllDayStock()
            val dayAverageFlow = mainActivityRepository.getAllDayAverage()

            combine(stockFlow, dayAverageFlow) { stockResult, dayAverageResult ->
                when {
                    stockResult is ApiResult.Success && dayAverageResult is ApiResult.Success -> {
                        val stockData = stockResult.data
                        val dayAverageData = dayAverageResult.data
                        val averageCodeMap = dayAverageData.associateBy { it.code }
                        val stockList = stockData.map {
                            val datAverage = averageCodeMap[it.code]
                            it.monthlyAveragePrice = datAverage?.monthlyAveragePrice ?: "0"
                            it
                        }
                        val sortedList = stockList.sortedByDescending { it.code }
                        _allStockList.value = sortedList
                    }
                    else -> {
                        // 處理錯誤情況
                        Log.e("MainActivityViewModel", "載入股票資料失敗")
                    }
                }
            }.collect()
        } finally {
            // 完成載入，隱藏 loading
            _isLoading.value = false
        }
    }

    fun changeSort(sort: SortedEnum) {
        when(sort) {
            SortedEnum.DESC -> {
                _allStockList.value = _allStockList.value.sortedByDescending { it.code }
            }
            SortedEnum.ASC -> {
                _allStockList.value = _allStockList.value.sortedBy { it.code }
            }
        }
    }
}