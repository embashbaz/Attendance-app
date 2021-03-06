package com.example.attendanceapp.domain.models

import com.example.attendanceapp.data.local.entity.AttendanceEntity

data class Attendance(
    val attendanceId: Long,
    val day: String,
    val time: String,
    val attendeeId: Int,
    val attendeeName: String,
    val eventId: Int,
    val eventName: String
){
    fun attendanceToAttendanceEntity(): AttendanceEntity{
        return AttendanceEntity(
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
