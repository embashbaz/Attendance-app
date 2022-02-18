package com.example.attendanceapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ValidateGetAllParticipantsTest {


    private lateinit var getEventParticipants: ValidateGetAttendeeParameters


    @Before
    fun setUp() {

        getEventParticipants = ValidateGetAttendeeParameters()

    }



    @Test
    fun `get all event participants with invalid id`() = runBlocking {
        val response = getEventParticipants(-1)
        assertThat(response.message).isEqualTo("Invalid event id")

    }



}