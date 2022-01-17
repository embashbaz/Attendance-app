package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddEvent @Inject constructor(private val repository: AttendanceMainRepository)
{
    suspend operator fun invoke(eventType: String, eventName: String): Flow<OperationStatus<String>> {
        if (eventType.isBlank() || eventName.isBlank()){
            return flow {
                emit(OperationStatus.Error<String>(message = "Event type and event name can not be blank"))
            }
        }
        val newEvent = Event(0, eventType, eventName, "")
        return repository.insertEvent(newEvent)
    }

}