package com.example.attendanceapp.presentation.check_attendance

import com.example.attendanceapp.domain.models.Attendance

data class CheckAttendanceFragmentState (
    val type: Int = 0,
    val eventId: Int = 0,
    val isLoading: Boolean = false,
    val attendance: List<Attendance> = emptyList()
)