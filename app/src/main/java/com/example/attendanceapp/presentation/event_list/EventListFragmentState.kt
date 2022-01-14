package com.example.attendanceapp.presentation.event_list

import com.example.attendanceapp.domain.models.Event

data class EventListFragmentState(
    val allEvent: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val noData: Boolean = false
)
