package com.example.attendanceapp.domain.models

import androidx.room.PrimaryKey

data class Event(
    val eventId: Int,
    val eventType: String,
    val eventName: String,
    val ownerId: String
    )
