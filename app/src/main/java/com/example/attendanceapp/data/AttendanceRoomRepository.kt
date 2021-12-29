package com.example.attendanceapp.data

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.local.AttendanceAppDao
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class AttendanceRoomRepository(val dao: AttendanceAppDao): AttendanceMainRepository {
    override suspend fun insertEvent(event: Event): Flow<OperationStatus<String>>  = flow{
        try {
            dao.insertEvent(event.eventToEventEntity())
             emit(OperationStatus.Success("record added"))

        }catch (e: Exception){
            emit(OperationStatus.Error("",message = e.toString()))
        }
    }

    override suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>> = flow{
        try {
            dao.insertAttendee(attendee.AttendeeToAttendeeEntity())
            emit(OperationStatus.Success("record added"))

        }catch (e: Exception){
            emit(OperationStatus.Error("",message = e.toString()))
        }
    }

    override suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>>  = flow{
        try {
            dao.insertAttendance(attendees.map { it.attendanceToAttendanceEntity() })
            emit(OperationStatus.Success("records added"))

        }catch (e: Exception){
            emit(OperationStatus.Error<String>(message = e.toString()))
        }
    }

    override suspend fun getAllEvents(): Flow<OperationStatus<List<Event>>>  = flow{
        try {
            val events = dao.getEvents().map { it.eventEntityToEvent() }
            emit(OperationStatus.Success(events))

        }catch (e: Exception){
           emit(OperationStatus.Error<List<Event>>(message = e.toString()))
        }
    }

    override suspend fun getAllParticipants(eventId: Int): Flow<OperationStatus<List<Attendee>>> = flow{
        try {
            val attendees = dao.getAttendeeByEvent(eventId).map { it.AttendeeEntityToAttendee() }
            emit(OperationStatus.Success(attendees))

        }catch (e: Exception){
            emit(OperationStatus.Error<List<Attendee>>(message = e.toString()))
        }
    }
}