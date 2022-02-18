package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.example.attendanceapp.domain.models.Event
import kotlinx.coroutines.runBlocking
import org.junit.Before

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


}