package com.rockex6.cathayunitedbanktest.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.nio.charset.Charset

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(request())
    }.let { response ->
        val body = response.body!!
        val contentType = body.contentType()
        val charset = contentType?.charset() ?: Charset.defaultCharset()
        val buffer = body.source().apply { request(Long.MAX_VALUE) }.buffer()
        val bodyContent = buffer.clone().readString(charset)
        Log.d("ApiResponse", "ApiResponse Url:  ${chain.request().url}")
        Log.d("ApiResponse", "ApiResponse Header: ${response.headers}")
        try {
            Log.d("ApiResponse", "ApiResponse Body: ${JSONObject(bodyContent).toString(4)}")
        } catch (e: Exception) {
            Log.e("ApiResponse", "ApiResponse Body: $bodyContent")
        }

        return@let response
    }
}