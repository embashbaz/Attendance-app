package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateRecordImageTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var updateRecordImage: UpdateRecordImage

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        updateRecordImage = UpdateRecordImage(repository)
    }

    @Test
    fun `update attendee with wrong id`() {
        runBlocking {
            val result = updateRecordImage(-3, "www.google.com/image").first()
            assertThat(result.message).isEqualTo("Error db id was invalid")
        }
    }

    @Test
    fun `update attendee with empty url`() {
        runBlocking {
            val result = updateRecordImage(3, "  ").first()
            assertThat(result.message).isEqualTo("An url was not returned")
        }
    }

    @Test
    fun `update attendee return an error`() {
        repository.returnDbError(true)
        runBlocking {
            val result = updateRecordImage(3, "www.google.com/image").first()
            assertThat(result.message).isEqualTo("Error")
        }

    }

    @Test
    fun `update attendee successfully`() {
        runBlocking {
            val result = updateRecordImage(3, "www.google.com/image").first()
            assertThat(result.message).isEqualTo("success")
        }
    }

}