package com.example.attendanceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.attendanceapp.domain.models.Attendance

@Entity
data class AttendanceEntity (
    @PrimaryKey val attendanceId: Float,
    val day: String,
    val time: String,
    val attendeeId: Int,
    val attendeeName: String,
    val eventId: Int,
    val eventName: String
    ){
    fun attendanceEntityToAttendance(): Attendance{
        return Attendance(
            attendanceId = attendanceId,
            day = day,
            time = time,
            attendeeId = attendeeId,
            attendeeName = attendeeName,
            eventId = eventId,
            eventName = eventName
        )

    }


}