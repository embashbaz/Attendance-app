package com.example.attendanceapp.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.local.AttendanceAppDao
import com.example.attendanceapp.data.local.entity.EventEntity
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import com.example.attendanceapp.domain.repository.Authenticator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AttendanceRoomRepository(val dao: AttendanceAppDao, val authenticator: Authenticator) :
    AttendanceMainRepository {

    companion object {
        const val PAGE_SIZE = 20
    }

    override suspend fun signIn(email: String, password: String): Flow<OperationStatus<String>> =
        flow {
            try {
                emit(authenticator.signInWithEmailAndPassword(email, password))
            } catch (e: Exception) {
                emit(OperationStatus.Error<String>(message = e.toString()))
            }
        }

    override suspend fun signUp(email: String, password: String): Flow<OperationStatus<String>> =
        flow {
            try {
                emit(authenticator.signUpWithEmailAndPassword(email, password))
            } catch (e: Exception) {

                emit(OperationStatus.Error<String>(message = e.toString()))
            }
        }

    override suspend fun getAuthStatus(): Flow<String> {
        return try {
            authenticator.getAuthStatus()
        } catch (e: Exception) {
            flow {
                emit("Error")
            }
        }
    }

    override suspend fun forgotPassword(email: String): Flow<OperationStatus<String>> =
        flow {
            try {
                emit(authenticator.forgotPassword(email))
            } catch (e: Exception) {

                emit(OperationStatus.Error<String>(message = e.toString()))
            }

        }

    override suspend fun insertEvent(event: Event): Flow<OperationStatus<String>> = flow {
        try {
            dao.insertEvent(event.eventToEventEntity())
            emit(OperationStatus.Success("record added"))

        } catch (e: Exception) {
            emit(OperationStatus.Error("", message = e.toString()))
        }
    }

    override suspend fun insertAttendee(attendee: Attendee): Flow<OperationStatus<String>> =
        channelFlow {
            try {
                val recordId = dao.insertAttendee(attendee.AttendeeToAttendeeEntity())
                send(OperationStatus.Success(data = recordId.toString()))

            } catch (e: Exception) {
                send(OperationStatus.Error("", message = e.toString()))
            }
        }

    override suspend fun insertAttendanceRecord(attendees: List<Attendance>): Flow<OperationStatus<String>> =
        flow {
            try {
                dao.insertAttendance(attendees.map { it.attendanceToAttendanceEntity() })
                emit(OperationStatus.Success("records added"))

            } catch (e: Exception) {
                emit(OperationStatus.Error<String>(message = e.toString()))
            }
        }

    override suspend fun updateAttendee(
        attendeeUrl: String,
        attendeeId: Int
    ): Flow<OperationStatus<String>> {
        return channelFlow {
            try {
                dao.update(attendeeUrl, attendeeId)
                send(OperationStatus.Success("records updated"))

            } catch (e: Exception) {
                send(OperationStatus.Error<String>(message = e.toString()))
            }
        }
    }

    override suspend fun getAllEvents(): Flow<PagingData<EventEntity>> {

        val pageConfig = PagingConfig(20)

        return Pager(pageConfig, null) {
            dao.getEvents()
        }.flow


    }

    override suspend fun getAllParticipants(eventId: Int): Flow<PagingData<Attendee>> {
        val pageConfig = PagingConfig(20)

        return Pager(pageConfig, null) {
            dao.getAttendeeByEvent(eventId)
        }.flow.map { it -> it.map { it.AttendeeEntityToAttendee() } }
    }

    override suspend fun getAllAttendance(eventId: Int): Flow<PagingData<Attendance>> {
        val pageConfig = PagingConfig(20)

        return Pager(pageConfig, null) {
            dao.getAllAttendance(eventId)
        }.flow.map { it -> it.map { it.attendanceEntityToAttendance() } }
    }

    override suspend fun getAttendanceByAttendee(
        eventId: Int,
        attendeeName: String
    ): Flow<PagingData<Attendance>> {
        val pageConfig = PagingConfig(20)

        return Pager(pageConfig, null) {
            dao.getAttendanceByAttendee(attendeeName, eventId)
        }.flow.map { it -> it.map { it.attendanceEntityToAttendance() } }
    }

    override suspend fun getAttendanceByDate(
        eventId: Int,
        day: String
    ): Flow<PagingData<Attendance>> {
        val pageConfig = PagingConfig(20)

        return Pager(pageConfig, null) {
            dao.getAttendanceByDay(day, eventId)
        }.flow.map { it -> it.map { it.attendanceEntityToAttendance() } }
    }

    override suspend fun logout(): Flow<OperationStatus<String>> = flow {
        try {
            emit(authenticator.logout())
        } catch (e: Exception) {

            Log.d("Error: ", e.toString())
            emit(OperationStatus.Error<String>(message = e.toString()))
        }
    }
}