package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAuthStatus @Inject constructor(val repository: AttendanceMainRepository){
    suspend operator fun invoke(): Flow<String>{
        return repository.getAuthStatus()

    }

}