package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.core.utils.getTodayToday
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddAttendee @Inject constructor (private val repository: AttendanceMainRepository) {
    suspend operator fun invoke(
        eventId: Int,
        name: String,
        pictureId: String
    ): Flow<OperationStatus<String>> {
        if (name.isBlank()) {
            return flow {
                emit(OperationStatus.Error<String>(message = "name can not be blank"))
            }

        } else if (eventId < 0){
            return flow {
                emit(
                    OperationStatus.Error<kotlin.String>(
                        message = "Invalid event Id"
                    )
                )
            }
        }
        val attendee = Attendee(0,eventId, name, getTodayToday(), pictureId)


        return repository.insertAttendee(attendee)


    }


}