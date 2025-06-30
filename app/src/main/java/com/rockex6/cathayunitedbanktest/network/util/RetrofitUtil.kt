package com.rockex6.cathayunitedbanktest.network.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rockex6.cathayunitedbanktest.network.ResponseInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    const val BASE_URL = "https://openapi.twse.com.tw/v1/"
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    private const val CONNECT_TIMEOUT = 30L
    private const val CALL_TIMEOUT = 30L

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
    }

    private fun getOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient
            .Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(ResponseInterceptor())


    private fun getGson(): Gson {
        return GsonBuilder().setLenient().create()
    }
}