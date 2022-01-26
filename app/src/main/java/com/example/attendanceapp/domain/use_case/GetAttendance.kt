package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAttendance @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(
        type: Int,
        eventId: Int,
        query: String
    ): Flow<OperationStatus<List<Attendance>>> {

       // if (type !in 3..-1) {

        //}

        if (eventId < 1) {
            return flow {
                emit(OperationStatus.Error(message = "Invalid event id"))

            }
        }

        if (type == 0) {
            return repository.getAllAttendance(eventId)
        } else if (type == 1) {
            return repository.getAttendanceByAttendee(eventId, query)
        } else if (type == 2){
            return repository.getAttendanceByDate(eventId, query)
        }else {
            return flow {
                emit(OperationStatus.Error(message = "Invalid type"))

            }
        }

    }
}