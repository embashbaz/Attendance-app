package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Register @Inject constructor(val attendanceMainRepository: AttendanceMainRepository){


    suspend operator fun invoke (email: String, password: String, confirmPassword: String): Flow<OperationStatus<String>> {
        if (email.isBlank()){
            return flow {
                emit(OperationStatus.Error(message = "email cannot be empty"))
            }
        }

        if(password.isBlank()){
            return flow {
                emit(OperationStatus.Error(message = "password cannot be empty"))
            }
        }

        if (password!= confirmPassword){
            return flow {
                emit(OperationStatus.Error(message = "Both password need to be the same"))
            }
        }

        return attendanceMainRepository.signUp(email.trim(), password)

    }
}