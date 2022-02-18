package com.example.attendanceapp.presentation.check_attendance

data class CheckAttendanceFragmentState (
    val type: Int = 0,
    val eventId: Int = 0,
    val isLoading: Boolean = false
)