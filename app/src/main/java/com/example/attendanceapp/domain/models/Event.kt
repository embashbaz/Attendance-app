package com.example.attendanceapp.domain.models

import android.os.Parcelable
import com.example.attendanceapp.data.local.entity.EventEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val eventId: Int = 0,
    val eventType: String = "",
    val eventName: String = "",
    val ownerId: String = ""
    ): Parcelable{
    fun eventToEventEntity(): EventEntity{
        return EventEntity(
            eventId = eventId,
            eventType = eventType,
            ownerId = ownerId,
            eventName = eventName
        )

    }
}
