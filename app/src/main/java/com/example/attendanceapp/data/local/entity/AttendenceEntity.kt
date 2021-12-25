package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttendenceEntity (
    @PrimaryKey val attendenceId: Float,
    val day: String,
    val time: String,
    val attendeeId: Int,
    val attendeeName: String,
    val eventId: Int,
    val eventName: String
    )