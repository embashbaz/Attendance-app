package com.example.attendanceapp.di

import android.app.Application
import androidx.room.Room
import com.example.attendanceapp.data.AttendanceRoomRepository
import com.example.attendanceapp.data.local.AttendanceAppDatabase
import com.example.attendanceapp.data.remote.FirebaseAuthenticator
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import com.example.attendanceapp.domain.repository.Authenticator
import com.example.attendanceapp.domain.use_case.AddAttendee
import com.example.attendanceapp.domain.use_case.UpdateRecordImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
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

    @Singleton
    @Provides
    fun provideAddAttendee(repository: AttendanceMainRepository): AddAttendee {
        return AddAttendee(repository)
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()


    @Singleton
    @Provides
    fun provideUpdateImageUrl(repository: AttendanceMainRepository): UpdateRecordImage{
        return UpdateRecordImage(repository)
    }

    @Provides
    @Singleton
    fun provideAuthenticator(auth: FirebaseAuth): Authenticator = FirebaseAuthenticator(auth)

    @Provides
    @Singleton
    fun provideAttendanceDatabase(app: Application): AttendanceAppDatabase{
        return Room.databaseBuilder(
            app, AttendanceAppDatabase::class.java, "attendance_db"
        )
            .build()
    }
}