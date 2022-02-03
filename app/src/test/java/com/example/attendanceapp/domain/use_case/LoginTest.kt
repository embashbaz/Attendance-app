package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginTest {

    private lateinit var repository: AttendanceFakeRepoImpl
    private lateinit var login: Login

    @Before
    fun setUp() {
        repository = AttendanceFakeRepoImpl()
        login = Login(repository)
    }


    @Test
    fun `sign in without password`(){
        runBlocking {
            val response = login("email", "").first()
            assertThat(response.message).isEqualTo("Email and password can not be empty")
        }
    }

    @Test
    fun `sign in with error`(){
        runBlocking {
            repository.returnDbError(true)
            val response = login("email", "thisthis").first()
            assertThat(response.message).isEqualTo("Error")
        }
    }

    @Test
    fun `sign in successfully`(){
        runBlocking {
            repository.returnDbError(false)
            val response = login("email", "thisthis").first()
            assertThat(response.data).isEqualTo("user logged in")
        }
    }
}