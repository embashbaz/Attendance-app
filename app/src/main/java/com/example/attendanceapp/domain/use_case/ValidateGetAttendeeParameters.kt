package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import javax.inject.Inject

class ValidateGetAttendeeParameters @Inject constructor() {
    suspend operator fun invoke(
        eventId: Int
    ): OperationStatus<Boolean> {

        if (eventId < 1) {
            return OperationStatus.Error(message = "Invalid event id")
        }
        return OperationStatus.Success(true)
    }


}