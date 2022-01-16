package com.example.attendanceapp.di

import android.app.Application
import androidx.room.Room
import com.example.attendanceapp.data.AttendanceRoomRepository
import com.example.attendanceapp.data.local.AttendanceAppDatabase
import com.example.attendanceapp.domain.repository.AttendanceMainRepository
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
    fun provideMainRepository(db: AttendanceAppDatabase): AttendanceMainRepository{
        return AttendanceRoomRepository(db.dao)
    }

    fun provideAttendanceDatabase(app: Application): AttendanceAppDatabase{
        return Room.databaseBuilder(
            app, AttendanceAppDatabase::class.java, "attendance_db"
        )
            .build()
    }
}