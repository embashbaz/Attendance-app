package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.core.utils.getTime
import com.example.attendanceapp.core.utils.getTodayToday
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddNewAttendance @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(
        eventId: Int,
        eventName: String,
        attendees: List<Attendee>
    ): Flow<OperationStatus<String>> {
        if (eventId < 1) {
            return flow {
                emit(OperationStatus.Error(message = "Error: Event Id is invalid"))
            }
        }

        if (eventName.isNullOrBlank()) {
            return flow {
                emit(OperationStatus.Error(message = "Error: Event name is empty"))
            }
        }

        val attendance = mutableListOf<Attendance>()
        for (item in attendees) {
            attendance.add(
                Attendance(
                    0F,
                    getTodayToday(),
                    getTime(),
                    item.personDbId,
                    item.name,
                    eventId,
                    eventName
                )
            )

        }

        return repository.insertAttendanceRecord(attendance)


    }

}