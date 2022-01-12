package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.example.attendanceapp.domain.models.Event
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllEventsTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var getAllEVent: GetAllEvents

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        getAllEVent = GetAllEvents(repository)
        insertEvents()
    }

    fun insertEvents() {
        runBlocking {
            ('a'..'z').forEachIndexed { index, c ->
                val event = Event(index, c.toString(), c.toString(), c.toString())
                repository.insertEvent(event)
            }
        }
    }

    @Test
    fun `get all events successfully`() = runBlocking {
        val response = getAllEVent().first()
        Truth.assertThat(response.message).isEqualTo("success")
    }
}