package com.example.attendanceapp.presentation.login

data class LoginFragmentUIState (
    val isLoading : Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false
)