package com.example.attendanceapp.domain.repository

interface Authenticator {

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun signInWithEmailAndPassword(email: String, password: String)
}