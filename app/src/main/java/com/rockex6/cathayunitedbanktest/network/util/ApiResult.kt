package com.rockex6.cathayunitedbanktest.network.util

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure<T>(val errorMessage: String) : ApiResult<T>()
}