package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.example.attendanceapp.domain.models.Attendee
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNewAttendanceTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var addNewAttendance: AddNewAttendance


    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        addNewAttendance = AddNewAttendance(repository)
    }

    @Test
    fun `add attendance with invalid event id`() = runBlocking {
        val testResult = addNewAttendance(0, "this", emptyList()).first()
        assertThat(testResult.message).isEqualTo("Error: Event Id is invalid")
    }

    @Test
    fun `add attendance with empty event name`() = runBlocking {
        @Test
        fun `add attendance with invalid event id`() = runBlocking {
            val testResult = addNewAttendance(45, "", emptyList()).first()
            assertThat(testResult.message).isEqualTo("Error: Event name is empty")
        }
    }

    @Test
    fun `add attendance successfully`() = runBlocking {
        val attendees = mutableListOf<Attendee>()
        ('a'..'z').forEachIndexed { index, c ->
            attendees.add(Attendee(index, Math.random().toInt(), c.toString(), "2022-4-5", ""))
        }
        val testResult = addNewAttendance(2, "that", attendees).first()
        assertThat(testResult.data).isEqualTo("record added")

    }

    @Test
    fun `add attendance unsuccessfully`() = runBlocking {
        val attendees = mutableListOf<Attendee>()
        repository.returnDbError(true)
        ('a'..'z').forEachIndexed { index, c ->
            attendees.add(Attendee(index, Math.random().toInt(), c.toString(), "2022-4-5", ""))
        }
        val testResult = addNewAttendance(2, "that", attendees).first()
        assertThat(testResult.message).isEqualTo("Error")


    }

}