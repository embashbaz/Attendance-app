package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import javax.inject.Inject

class ValidateGetAllParticipantParameters @Inject constructor(){

    suspend operator fun invoke(
        type: Int,
        eventId: Int
    ): OperationStatus<Boolean> {

        if (eventId < 1) {
          return OperationStatus.Error(message = "Invalid event id")
        }

        if (type < 0 || type > 2){
            return OperationStatus.Error(message = "Invalid type")
        }

        return OperationStatus.Success(true)
    }
}