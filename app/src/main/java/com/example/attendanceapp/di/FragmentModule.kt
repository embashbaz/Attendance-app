package com.example.attendanceapp.di

import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import com.example.attendanceapp.domain.use_case.AddEvent
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import com.example.attendanceapp.domain.use_case.GetAllEvents
import com.example.attendanceapp.domain.use_case.SignOut
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {



    @Provides
    fun provideAddEvent(repository: AttendanceMainRepository): AddEvent {
        return AddEvent(repository)
    }

    @Provides
    fun provideGetParticipants(repository: AttendanceMainRepository): GetAllEventParticipant{
        return GetAllEventParticipant(repository)
    }

    @Provides
    fun provideGetAllEvents(repository: AttendanceMainRepository): GetAllEvents {
        return GetAllEvents(repository)
    }

    @Provides
    fun provideSignOut(repository: AttendanceMainRepository): SignOut{
        return SignOut(repository)
    }

}