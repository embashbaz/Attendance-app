package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRecordImage @Inject constructor(val repo: AttendanceMainRepository){

    suspend operator fun invoke(personId: Int, url: String): Flow<OperationStatus<String>> {
            if (personId < 1){
                return flow {
                    emit(OperationStatus.Error(message = "Error id was null"))
                }
            }

             if (url.isBlank()){
                 return flow {
                     emit(OperationStatus.Error(message = "An url was not returned"))
                 }
            }

        return repo.updateAttendee(url, personId)
    }

}