package com.example.attendanceapp.domain.repository

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import kotlinx.coroutines.flow.Flow

interface AttendanceMainRepository {

    suspend fun insertEvent(event: Event): Flow<OperationStatus<String>>

    suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>>

    suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>>

    suspend fun getAllEvents(): Flow<OperationStatus<List<Event>>>

    suspend fun getAllParticipants(eventId: Int): Flow<OperationStatus<List<Attendee>>>





}