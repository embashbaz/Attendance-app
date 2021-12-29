package com.example.attendanceapp.domain.models

import androidx.room.PrimaryKey
import com.example.attendanceapp.data.local.entity.EventEntity

data class Event(
    val eventId: Int,
    val eventType: String,
    val eventName: String,
    val ownerId: String
    ){
    fun eventToEventEntity(): EventEntity{
        return EventEntity(
            eventId = eventId,
            eventType = eventType,
            ownerId = ownerId,
            eventName = eventName
        )

    }
}
