package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddEventTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var addEvent: AddEvent


    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        addEvent = AddEvent(repository)

    }

    @Test
    fun `add event with blank type`() = runBlocking{
        val response = addEvent("", "this").first()
        assertThat(response.message).isEqualTo("Event type and event name can not be blank")

    }

    @Test
    fun `add event with blank name`() = runBlocking{
        val response = addEvent("this", "").first()
        assertThat(response.message).isEqualTo("Event type and event name can not be blank")

    }

    @Test
    fun `add event with db error`()= runBlocking{
        repository.returnDbError(true)
        val response = addEvent("this", "this").first()
        assertThat(response.message).isEqualTo("Error")

    }

    @Test
    fun `add event successfully`()= runBlocking{
        repository.returnDbError(false)
        val response = addEvent("this", "this").first()
        assertThat(response.data).isEqualTo("records added")

    }


}