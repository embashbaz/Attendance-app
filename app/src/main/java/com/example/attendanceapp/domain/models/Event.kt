package com.example.attendanceapp.domain.models

import androidx.room.PrimaryKey

data class Event(
    val eventId: Int,
    val enventType: String,
    val ownerId: String
    )
