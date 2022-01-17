package com.example.attendanceapp.presentation.new_attendance

import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event

data class NewAttendanceFragmentState (
    val allParticipants: List<Attendee> = emptyList(),
    val eventObject: Event = Event()

        )