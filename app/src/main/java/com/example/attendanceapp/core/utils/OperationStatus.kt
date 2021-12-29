package com.example.attendanceapp.core.utils

sealed class OperationStatus<T>( val data: T? = null, val message: String? = null){

    class Success<T>(data: T): OperationStatus<T>(data, "success")
    class Error<T>(data: T, message: String): OperationStatus<T>(data,message)
    class Loading<T> (data: T? = null): OperationStatus<T>(data)
    object Empty

}
