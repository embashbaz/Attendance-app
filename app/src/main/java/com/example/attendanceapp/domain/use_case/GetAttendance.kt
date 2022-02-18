package com.example.attendanceapp.domain.use_case

import androidx.paging.PagingData
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAttendance @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(
        type: Int,
        eventId: Int,
        query: String
    ): Flow<PagingData<Attendance>> {


        if (type == 1) {
            return repository.getAttendanceByAttendee(eventId, query)
        } else if (type == 2) {
            return repository.getAttendanceByDate(eventId, query)
        } else {
            return repository.getAllAttendance(eventId)
        }

    }
}