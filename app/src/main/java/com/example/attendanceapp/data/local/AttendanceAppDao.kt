package com.example.attendanceapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.attendanceapp.data.local.entity.AttendanceEntity
import com.example.attendanceapp.data.local.entity.AttendeeEntity
import com.example.attendanceapp.data.local.entity.EventEntity
import com.example.attendanceapp.domain.models.Attendance
import kotlinx.coroutines.flow.Flow


@Dao
interface AttendanceAppDao {

    @Insert
    fun insertAttendee(attendee: AttendeeEntity)

    @Insert
    fun insertEvent(event: EventEntity)

    @Insert
    fun insertAttendance(attendees: List<AttendanceEntity> )

    @Query("SELECT * FROM AttendanceEntity WHERE eventId = :eventId ORDER BY day DESC")
    fun getAttendanceByEvent(eventId: Int): Flow<List<Attendance>>

    @Query("SELECT * FROM AttendanceEntity WHERE attendeeId = :attendeeId AND eventId = :eventId ORDER BY day DESC")
    fun getAttendanceByAttendee(attendeeId: Int, eventId: Int): Flow<List<Attendance>>

    @Query("SELECT * FROM AttendanceEntity WHERE day = :day AND eventId = :eventId ORDER BY time DESC")
    fun getAttendanceByDay(day: String, eventId: Int): Flow<List<Attendance>>

    @Query("SELECT * FROM AttendeeEntity WHERE eventDbId = :eventId ORDER BY personDbId DESC")
    fun getAttendeeByEvent(eventId: Int): Flow<List<AttendeeEntity>>

    @Query("SELECT * FROM EventEntity ORDER BY eventId DESC")
    fun getEvents(): Flow<List<EventEntity>>
}