package com.example.attendanceapp.domain.use_case

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ValidateGetAttendanceParametersTest {


    private lateinit var getAttendance: ValidateGetAllParticipantParameters

    @Before
    fun setUp() {

        getAttendance = ValidateGetAllParticipantParameters()

    }


    @Test
    fun `get attendance with invalid event id`() = runBlocking {
        val response = getAttendance(2, 0)
        Truth.assertThat(response.message).isEqualTo("Invalid event id")
    }

    @Test
    fun `get attendance with invalid type`() = runBlocking {
        val response = getAttendance(6, 1)
        Truth.assertThat(response.message).isEqualTo("Invalid type")
    }

}

