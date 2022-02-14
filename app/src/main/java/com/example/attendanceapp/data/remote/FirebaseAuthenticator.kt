package com.example.attendanceapp.data.remote

import com.example.attendanceapp.core.utils.OperationStatus
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

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): OperationStatus<String> {OperationStatus.Success("Success")


        try {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
            return OperationStatus.Success("success")
        }catch (e: Exception){
            return OperationStatus.Error(message = e.toString())
        }

    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) : OperationStatus<String>  {
        try {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .continueWith {
                    if (it.isSuccessful){
                       // return OperationStatus.Success("success")
                    }else {

                    }


                }
            return OperationStatus.Success("success")

        }catch (e: Exception){
            return OperationStatus.Error(message = e.toString())
        }

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

    override suspend fun forgotPassword(email: String): OperationStatus<String>  {

        try {
            mFirebaseAuth.sendPasswordResetEmail(email)
            return OperationStatus.Success("success")
        }catch (e: Exception){
            return OperationStatus.Error(message = e.toString())
        }
    }

    override suspend fun logout(): OperationStatus<String> {
           try {
            mFirebaseAuth.signOut()
            return OperationStatus.Success("success")
        }catch (e: Exception){
            return OperationStatus.Error(message = e.toString())
        }
    }
}