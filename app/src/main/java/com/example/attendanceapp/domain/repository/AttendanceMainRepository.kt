package com.example.attendanceapp.domain.repository

import androidx.paging.PagingData
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.local.entity.EventEntity
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import kotlinx.coroutines.flow.Flow

interface AttendanceMainRepository {

    suspend fun signIn(email: String, password: String): Flow<OperationStatus<String>>

    suspend fun signUp(email: String, password: String): Flow<OperationStatus<String>>

    suspend fun insertEvent(event: Event): Flow<OperationStatus<String>>

    suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>>

    suspend fun updateAttendee(attendeeUrl: String, attendeeId: Int): Flow<OperationStatus<String>>

    suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>>

    suspend fun getAllEvents(): Flow<PagingData<EventEntity>>

    suspend fun getAllParticipants(eventId: Int): Flow<PagingData<Attendee>>

    suspend fun getAllAttendance(eventId: Int): Flow<PagingData<Attendance>>

    suspend fun getAttendanceByAttendee(eventId: Int, attendeeName: String): Flow<PagingData<Attendance>>

    suspend fun getAttendanceByDate(eventId: Int, day: String): Flow<PagingData<Attendance>>

    suspend fun getAuthStatus(): Flow<String>

    suspend fun logout(): Flow<OperationStatus<String>>

    suspend fun forgotPassword(email: String): Flow<OperationStatus<String>>



}