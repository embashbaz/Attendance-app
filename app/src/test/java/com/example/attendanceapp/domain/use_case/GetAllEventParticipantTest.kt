package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.example.attendanceapp.domain.models.Attendee
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllEventParticipantTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var getEventParticipants: GetAllEventParticipant


    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        getEventParticipants = GetAllEventParticipant(repository)
        insertEvents()
    }

    fun insertEvents(){
        runBlocking {
            ('a'..'z').forEachIndexed { index, c ->
                val attendee = Attendee(index, index, c.toString(), c.toString(), "")
                repository.insertAttendee(attendee)
            }
        }


    }

    @Test
    fun `get all event participants with invalid id`() = runBlocking {
        val response = getEventParticipants(-1).first()
        assertThat(response.message).isEqualTo("Invalid event id")

    }

    @Test
    fun `get all event successfully`() = runBlocking {
        val response = getEventParticipants(3).first()
        assertThat(response.message).isEqualTo("success")

    }

}