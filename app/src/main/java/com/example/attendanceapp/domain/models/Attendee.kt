package com.example.attendanceapp.domain.models

import com.example.attendanceapp.data.local.entity.AttendeeEntity

data class Attendee (
    val personDbId: Int,
    val eventDbId: Int,
    val name: String,
    val dateAdded: String,
    val pictureId: String
){

    fun AttendeeToAttendeeEntity(): AttendeeEntity{
        return AttendeeEntity(
            personDbId = personDbId,
            eventDbId = eventDbId,
            name = name,
            dateAdded = dateAdded,
            pictureId = pictureId
        )

    }
}