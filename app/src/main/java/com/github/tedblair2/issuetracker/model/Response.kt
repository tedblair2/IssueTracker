package com.github.tedblair2.issuetracker.model

sealed class Response<T>(val data:T?=null,val error:String?=null) {
    class Success<T>(data: T?):Response<T>(data = data)
    class Error<T>(error: String?):Response<T>(error = error)
    class Loading<T>:Response<T>()
}