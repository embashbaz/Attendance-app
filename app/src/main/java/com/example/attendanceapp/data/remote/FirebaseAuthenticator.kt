package com.example.attendanceapp.data.remote

import com.example.attendanceapp.domain.repository.Authenticator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor(private val mFirebaseAuth: FirebaseAuth) :
    Authenticator {

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)

    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {

        mFirebaseAuth.signInWithEmailAndPassword(email, password)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAuthStatus(): Flow<String> = callbackFlow {
        mFirebaseAuth.addAuthStateListener {

            if (it.currentUser != null) {
                it.currentUser!!.reload()
                trySend("logged in")

            } else {
                trySend("logged out")
            }

        }
        awaitClose {
            cancel()
        }


    }

    override suspend fun forgotPassword(email: String) {
        mFirebaseAuth.sendPasswordResetEmail(email)
    }

    override suspend fun logout(){
        mFirebaseAuth.signOut()
    }
}