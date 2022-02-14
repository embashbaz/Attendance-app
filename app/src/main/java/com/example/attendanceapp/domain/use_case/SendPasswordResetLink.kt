package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendPasswordResetLink @Inject constructor(val attendanceMainRepository: AttendanceMainRepository){


    suspend operator fun invoke (email: String, confirmEmail: String): Flow<OperationStatus<String>> {
        if (email.isBlank() || confirmEmail.isBlank()){
            return flow {
                emit(OperationStatus.Error(message = "Please give the email twice"))
            }
        }

        if (email != confirmEmail){
            return flow {
                emit(OperationStatus.Error(message = "Both email must be the same"))
            }
        }

        return attendanceMainRepository.forgotPassword(email.trim())
    }
}