package com.example.attendanceapp.domain.models

import androidx.room.PrimaryKey

data class Attendance(
    val attendanceId: Float,
    val day: String,
    val time: String,
    val attendeeId: Int,
    val attendeeName: String,
    val eventId: Int,
    val eventName: String
)
