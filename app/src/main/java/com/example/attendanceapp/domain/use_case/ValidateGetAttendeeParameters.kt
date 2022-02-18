package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus

class ValidateGetAttendeeParameters {
    suspend operator fun invoke(
        eventId: Int
    ): OperationStatus<Boolean> {

        if (eventId < 1) {
            return OperationStatus.Error(message = "Invalid event id")
        }
        return OperationStatus.Success(true)
    }


}