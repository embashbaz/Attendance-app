package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.example.attendanceapp.domain.models.Attendance
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAttendanceTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var getAttendance: GetAttendance

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        getAttendance = GetAttendance(repository)

    }

    private fun insertAttendanceParticipants() {
        runBlocking {
            val attendees = mutableListOf<Attendance>()
            ('a'..'z').forEachIndexed { index, c ->
                attendees.add(Attendance(index.toLong(), c.toString(),c.toString(), index, c.toString(),index,c.toString()))
            }
            repository.insertAttendanceRecord(attendees)

        }

    }

    @Test
    fun `get attendance with invalid event id`() = runBlocking{
        val response = getAttendance(2,0,"this").first()
        Truth.assertThat(response.message).isEqualTo("Invalid event id")
    }

    @Test
    fun `get attendance with invalid type`() = runBlocking{
        val response = getAttendance(6,1,"this").first()
        Truth.assertThat(response.message).isEqualTo("Invalid type")
    }

    @Test
    fun `get all attendance successfully `() = runBlocking{
        insertAttendanceParticipants()
        val response = getAttendance(0,1,"b").first()
        Truth.assertThat(response.message).isEqualTo("success")
    }


}

