package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.data.AttendanceFakeRepoImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RegisterTest {

    private lateinit var repo: AttendanceFakeRepoImpl
    private lateinit var register: Register

    @Before
    fun setUp() {
        repo = AttendanceFakeRepoImpl()
        register = Register(repo)
    }


    @Test
    fun `test with empty email`(){
        runBlocking {
            val response = register("", "this", "this").first()
            assertThat(response.message).isEqualTo("email cannot be empty")
        }
    }
    @Test
    fun `test with empty  password`(){
        runBlocking {
            val response = register("this", "", "this").first()
            assertThat(response.message).isEqualTo("password cannot be empty")
        }
    }
    @Test
    fun `test with different password`(){
        runBlocking {
            val response = register("htis", "this", "thas").first()
            assertThat(response.message).isEqualTo("Both password need to be the same")
        }
    }

    @Test
    fun `register successfully`(){
        runBlocking {
            val response = register("those", "this", "this").first()
            assertThat(response.message).isEqualTo("success")
        }
    }

    @Test
    fun `register return error`(){
        repo.returnDbError(true)
        runBlocking {
            val response = register("htis", "this", "this").first()
            assertThat(response.message).isEqualTo("Error")
        }
    }
}