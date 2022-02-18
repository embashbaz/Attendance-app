package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Login @Inject constructor(val repo: AttendanceMainRepository){

    suspend operator fun invoke(email: String, password: String): Flow<OperationStatus<String>> {

        if (email.isBlank() || password.isBlank()){
            return flow {
                emit(OperationStatus.Error(message = "Email and password can not be empty"))
            }
        }

        return repo.signIn(email.trim(), password)

    }

}