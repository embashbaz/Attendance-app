package com.example.attendanceapp.data.remote

import com.example.attendanceapp.domain.repository.Authenticator
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor(private val mFirebaseAuth: FirebaseAuth) :
    Authenticator {

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)

    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {

        mFirebaseAuth.signInWithEmailAndPassword(email, password)

    }
}