package com.example.attendanceapp.domain.models

import androidx.room.PrimaryKey

data class Attendee (
    val personDbId: Int,
    val eventDbId: Int,
    val name: Int,
    val dateAdded: String,
    val pictureId: String
)