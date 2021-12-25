package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventEntity (
        @PrimaryKey val eventId: Int,
        val enventType: String,
        val ownerId: String,

        )