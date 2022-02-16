package com.example.attendanceapp.presentation.event_list

import androidx.paging.PagingData
import com.example.attendanceapp.domain.models.Event

data class EventListFragmentState(
    val allEvent: PagingData<Event> = PagingData.empty(),
    val isLoading: Boolean = false,
    val logOut: Boolean = false
)
