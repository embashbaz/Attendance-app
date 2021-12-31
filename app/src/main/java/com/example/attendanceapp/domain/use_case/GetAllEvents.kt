package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow

class GetAllEvents (private val repository: AttendanceMainRepository){

    suspend operator fun invoke(): Flow<OperationStatus<List<Event>>> {
        return repository.getAllEvents()

    }

}