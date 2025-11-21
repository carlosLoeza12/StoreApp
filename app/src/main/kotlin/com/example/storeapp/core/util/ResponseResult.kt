package com.example.storeapp.core.util

sealed class ResponseResult<out T> {

    data class Success<out T>(val value: T): ResponseResult<T>()

    data class Error(val message: String, val cause: Throwable? = null): ResponseResult<Nothing>()

    object Loading: ResponseResult<Nothing>()
}