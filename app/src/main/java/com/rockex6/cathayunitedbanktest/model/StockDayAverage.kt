package com.rockex6.cathayunitedbanktest.model

import com.google.gson.annotations.SerializedName

/**
 * Data class for STOCK_DAY_AVG_ALL API response
 * 股票日均價資訊模型
 */
data class StockDayAverage(
    @SerializedName("Date") val date: String,
    @SerializedName("Code") val code: String,                   // 股票代號
    @SerializedName("Name") val name: String,                   // 股票名稱
    @SerializedName("ClosingPrice") val closingPrice: String,           // 收盤價
    @SerializedName("MonthlyAveragePrice") val monthlyAveragePrice: String     // 月平均價
)
