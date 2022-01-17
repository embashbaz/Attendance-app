package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.attendanceapp.domain.models.Event

@Entity
data class EventEntity (
        @PrimaryKey(autoGenerate = true)
        val eventId: Int,
        val eventType: String,
        val eventName: String,
        val ownerId: String,
        ){
        fun eventEntityToEvent(): Event{
                return Event(
                        eventId = eventId,
                        eventType = eventType,
                        ownerId = ownerId,
                        eventName = eventName
                )

        }
}