package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SendPasswordResetLinkTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var sendResetPassword: SendPasswordResetLink

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        sendResetPassword = SendPasswordResetLink(repository)
    }

    @Test
    fun `send reset email with empty email field`() {
        runBlocking {
            val result = sendResetPassword("", "this@gmail.com").first()
            assertThat(result.message).isEqualTo("Please give the email twice")

        }
    }

    @Test
    fun `send reset email with two different emails`() {
        runBlocking {
            val result = sendResetPassword("that@gmail.com", "this@gmail.com").first()
            assertThat(result.message).isEqualTo("Both email must be the same")

        }
    }

    @Test
    fun `send reset email with error`() {
        repository.returnDbError(true)
        runBlocking {
            val result = sendResetPassword("this@gmail.com", "this@gmail.com").first()
            assertThat(result.message).isEqualTo("Error")

        }

    }

    @Test
    fun `send reset email successfully`() {
        repository.returnDbError(false)
        runBlocking {
            val result = sendResetPassword("this@gmail.com", "this@gmail.com").first()
            assertThat(result.message).isEqualTo("success")

        }
    }
}