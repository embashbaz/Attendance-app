package com.example.attendanceapp.domain.use_case

import androidx.paging.PagingData
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEventParticipant @Inject constructor(private val repository: AttendanceMainRepository) {

    suspend operator fun invoke(eventId: Int): Flow<PagingData<Attendee>> {

        return repository.getAllParticipants(eventId)
    }


}