package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEventParticipant @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(eventId: Int): Flow<OperationStatus<List<Attendee>>> {
        if (eventId < 0) {
            return flow {
                emit(OperationStatus.Error(message = "Invalid event id"))

            }
        }

        return repository.getAllParticipants(eventId)
    }


}