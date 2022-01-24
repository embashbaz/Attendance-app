package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAttendance @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(type: Int, query: String): Flow<OperationStatus<List<Attendance>>> {


    }
}