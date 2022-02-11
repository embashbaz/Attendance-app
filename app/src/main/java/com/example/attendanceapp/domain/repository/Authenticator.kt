package com.example.attendanceapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface Authenticator {

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun getAuthStatus(): Flow<String>

    suspend fun logout()
}