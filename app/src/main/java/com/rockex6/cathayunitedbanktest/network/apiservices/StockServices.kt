package com.rockex6.cathayunitedbanktest.network.apiservices

import com.rockex6.cathayunitedbanktest.network.ApiPath
import com.rockex6.cathayunitedbanktest.model.StockDetailInfo
import com.rockex6.cathayunitedbanktest.model.StockDayAverage
import com.rockex6.cathayunitedbanktest.model.StockTrading
import retrofit2.Response
import retrofit2.http.GET

interface StockServices {
    @GET(ApiPath.GET_STOCK_DETAIL)
    suspend fun getStockDetail(): Response<List<StockDetailInfo>>

    @GET(ApiPath.ALL_DAY_AVG)
    suspend fun getAllDayAverage(): Response<List<StockDayAverage>>

    @GET(ApiPath.ALL_DAY_STOCK)
    suspend fun getAllDayStock(): Response<List<StockTrading>>
}