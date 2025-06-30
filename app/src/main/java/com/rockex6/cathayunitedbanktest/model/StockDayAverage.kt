package com.rockex6.cathayunitedbanktest.model

/**
 * Data class for STOCK_DAY_AVG_ALL API response
 * 股票日均價資訊模型
 */
data class StockDayAverage(
    val code: String,                   // 股票代號
    val name: String,                   // 股票名稱
    val closingPrice: String,           // 收盤價
    val monthlyAveragePrice: String     // 月平均價
)
