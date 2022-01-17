package com.example.attendanceapp.presentation.event_detail

import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event

data class EventDetailFragmentState(
    val eventObject: Event = Event(),
    val participants: List<Attendee> = emptyList(),
    val isLoading: Boolean = false
)