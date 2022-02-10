package com.example.attendanceapp.presentation.take_attendance_camera

import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event

data class CameraAttendanceState (
    val eventObject: Event = Event(),
    val participants: List<Attendee> = emptyList(),
    val isLoading: Boolean = false,
    val isDetectingFaces: Boolean = false
)