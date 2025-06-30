package com.rockex6.cathayunitedbanktest.network.apiservices

import com.rockex6.cathayunitedbanktest.network.ApiPath
import com.rockex6.cathayunitedbanktest.model.StockBasicInfo
import com.rockex6.cathayunitedbanktest.model.StockDayAverage
import com.rockex6.cathayunitedbanktest.model.StockTrading
import retrofit2.Response
import retrofit2.http.GET

interface StockServices {
    @GET(ApiPath.ALL_STOCK)
    suspend fun getAllStock(): Response<List<StockBasicInfo>>

    @GET(ApiPath.ALL_DAY_AVG)
    suspend fun getAllDayAverage(): Response<List<StockDayAverage>>

    @GET(ApiPath.ALL_DAY_STOCK)
    suspend fun getAllDayStock(): Response<List<StockTrading>>
}