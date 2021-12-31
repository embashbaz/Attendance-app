package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.attendanceapp.domain.models.Attendee

@Entity
data class AttendeeEntity (
    @PrimaryKey
    val personDbId: Int,
    val eventDbId: Int,
    val name: String,
    val dateAdded: String,
    val pictureId: String
    ){

    fun AttendeeEntityToAttendee(): Attendee{
        return Attendee(
            personDbId = personDbId,
            eventDbId = eventDbId,
            name = name,
            dateAdded =dateAdded,
            pictureId = pictureId
        )

    }
}