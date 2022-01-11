package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddAttendeeTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var addAttendee: AddAttendee

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        addAttendee = AddAttendee(repository)
    }

    @Test
    fun `add attendee with wrong event id`() = runBlocking {
        val response = addAttendee(-1, "this", "").first()
        assertThat(response.message).isEqualTo("Invalid event Id")

    }

    @Test
    fun `add attendee with empty name`() = runBlocking {
        val response = addAttendee(5, "", "").first()
        assertThat(response.message).isEqualTo("name can not be blank")

    }
    @Test
    fun `add attendee with dbError error`() =  runBlocking {
        repository.returnDbError(true)
        val response = addAttendee(5, "this", "").first()
        assertThat(response.message).isEqualTo("Error")

    }

    @Test
    fun `add attendee successfully`() =  runBlocking {
       repository.returnDbError(false)
        val response = addAttendee(5, "this", "").first()
        assertThat(response.data).isEqualTo("records added")

    }




}