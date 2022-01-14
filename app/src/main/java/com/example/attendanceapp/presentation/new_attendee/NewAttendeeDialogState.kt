package com.example.attendanceapp.presentation.new_attendee

data class NewAttendeeDialogState(
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)