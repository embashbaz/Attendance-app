package com.example.attendanceapp.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllEvents @Inject constructor(private val repository: AttendanceMainRepository){

    suspend operator fun invoke(): Flow<PagingData<Event>> {
        return repository.getAllEvents().map { it -> it.map { it.eventEntityToEvent() } }

    }

}