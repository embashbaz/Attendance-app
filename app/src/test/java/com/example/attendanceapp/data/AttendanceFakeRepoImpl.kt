package com.example.attendanceapp.data

import androidx.paging.PagingData
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.local.entity.EventEntity
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AttendanceFakeRepoImpl : AttendanceMainRepository {

    private val events = mutableListOf<Event>()
    private val attendees = mutableListOf<Attendee>()
    private val attendances = mutableListOf<Attendance>()
    private var dbError = false

    fun returnDbError(value: Boolean) {
        dbError = value
    }

    override suspend fun signIn(email: String, password: String): Flow<OperationStatus<String>> {
        return flow {
            if (!dbError)
                emit(OperationStatus.Success("user logged in"))
            else
                emit(OperationStatus.Error("", message = "Error"))
        }
    }

    override suspend fun signUp(email: String, password: String): Flow<OperationStatus<String>> {
        return flow {
            if (!dbError)
                emit(OperationStatus.Success("user added"))
            else
                emit(OperationStatus.Error("", message = "Error"))
        }
    }

    override suspend fun insertEvent(event: Event): Flow<OperationStatus<String>> {
        return flow {
            events.add(event)
            if (!dbError)
                emit(OperationStatus.Success("records added"))
            else
                emit(OperationStatus.Error("", message = "Error"))
        }
    }

    override suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>> {
        return flow {
            attendees.add(attendee)
            if (!dbError)
                emit(OperationStatus.Success("records added"))
            else
                emit(OperationStatus.Error("", message = "Error"))

        }
    }

    override suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>> {
        return flow {
            if (!dbError)
                emit(OperationStatus.Success("record added"))
            else
                emit(OperationStatus.Error<String>(message = "Error"))
        }
    }

    override suspend fun getAllEvents(): Flow<PagingData<EventEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllParticipants(eventId: Int): Flow<PagingData<Attendee>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAttendance(eventId: Int): Flow<PagingData<Attendance>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendanceByAttendee(
        eventId: Int,
        attendeeName: String
    ): Flow<PagingData<Attendance>> {
        return getAllAttendance(eventId)
    }

    override suspend fun getAttendanceByDate(
        eventId: Int,
        day: String
    ): Flow<PagingData<Attendance>> {
        return getAllAttendance(eventId)
    }


    override suspend fun updateAttendee(
        attendeeUrl: String,
        attendeeId: Int
    ): Flow<OperationStatus<String>> {
        return flow {
            if (!dbError)
                emit(OperationStatus.Success("record updated"))
            else
                emit(OperationStatus.Error("", message = "Error"))
        }
    }

    override suspend fun getAuthStatus(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Flow<OperationStatus<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(email: String): Flow<OperationStatus<String>> {
        return flow {
            if (!dbError)
                emit(OperationStatus.Success("email sent"))
            else
                emit(OperationStatus.Error("", message = "Error"))
        }
    }
}