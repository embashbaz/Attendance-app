package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttendeeEntity (
    @PrimaryKey
    val personDbId: Int,
    val eventDbId: Int,
    val name: Int,
    val dateAdded: String,
    val pictureId: String
    )