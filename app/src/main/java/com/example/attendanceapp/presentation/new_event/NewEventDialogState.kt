package com.example.attendanceapp.presentation.new_event

data class NewEventDialogState(
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)