package com.example.attendanceapp.domain.repository

import com.example.attendanceapp.core.utils.OperationStatus
import kotlinx.coroutines.flow.Flow

interface Authenticator {

    suspend fun signUpWithEmailAndPassword(email: String, password: String): OperationStatus<String>

    suspend fun signInWithEmailAndPassword(email: String, password: String): OperationStatus<String>

    suspend fun getAuthStatus(): Flow<String>

    suspend fun forgotPassword(email: String): OperationStatus<String>

    suspend fun logout(): OperationStatus<String>
}