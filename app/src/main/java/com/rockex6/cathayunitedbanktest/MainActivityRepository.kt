package com.rockex6.cathayunitedbanktest

import com.rockex6.cathayunitedbanktest.network.apiservices.StockServices
import com.rockex6.cathayunitedbanktest.network.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainActivityRepository(
    val stockServices: StockServices
) {
    fun getAllStock() = flow {
        val response = stockServices.getAllStock()
        if (response.isSuccessful) {
            val stockList = response.body()!!
            emit(ApiResult.Success(stockList))
        } else {
            response.errorBody()?.let {
                emit(ApiResult.Failure(it.toString()))
            }
        }
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(ApiResult.Failure(e.toString()))
    }

    fun getAllDayAverage() = flow {
        val stockList = stockServices.getAllDayAverage()
        if (stockList.isSuccessful) {
            emit(ApiResult.Success(stockList))
        } else {
            stockList.errorBody()?.let {
                emit(ApiResult.Failure(it.toString()))
            }
        }
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(ApiResult.Failure(e.toString()))
    }

    fun getAllDayStock() = flow {
        val response = stockServices.getAllDayStock()
        if (response.isSuccessful) {
            val stockList = response.body()!!
            emit(ApiResult.Success(stockList))
        } else {
            response.errorBody()?.let {
                emit(ApiResult.Failure(it.toString()))
            }
        }
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(ApiResult.Failure(e.toString()))
    }
}