package com.example.attendanceapp.data

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.local.AttendanceAppDao
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AttendanceRoomRepository(val dao: AttendanceAppDao): AttendanceMainRepository {
    override suspend fun insertEvent(event: Event): Flow<OperationStatus<String>>  = flow{
        try {
            dao.insertEvent(event.map{i})

        }catch (e: Exception){
            return emit(OperationStatus.Error(message = e.toString() ?: "An error occured"))
        }


    }

    override suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): Flow<OperationStatus<List<Event>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllParticipants(eventId: Int): Flow<OperationStatus<List<Attendee>>> {
        TODO("Not yet implemented")
    }
}