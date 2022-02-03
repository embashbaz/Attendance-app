package com.example.attendanceapp.di

import android.app.Application
import androidx.room.Room
import com.example.attendanceapp.data.AttendanceRoomRepository
import com.example.attendanceapp.data.local.AttendanceAppDatabase
import com.example.attendanceapp.data.remote.FirebaseAuthenticator
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import com.example.attendanceapp.domain.repository.Authenticator
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMainRepository(db: AttendanceAppDatabase, auth: Authenticator): AttendanceMainRepository{
        return AttendanceRoomRepository(db.dao, auth)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticator(auth: FirebaseAuth) = FirebaseAuthenticator(auth)

    @Provides
    @Singleton
    fun provideAttendanceDatabase(app: Application): AttendanceAppDatabase{
        return Room.databaseBuilder(
            app, AttendanceAppDatabase::class.java, "attendance_db"
        )
            .build()
    }
}