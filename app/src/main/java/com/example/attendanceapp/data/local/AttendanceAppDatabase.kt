package com.example.attendanceapp.data.local

import androidx.room.Database
import com.example.attendanceapp.data.local.entity.AttendanceEntity
import com.example.attendanceapp.data.local.entity.AttendeeEntity
import com.example.attendanceapp.data.local.entity.EventEntity

@Database(
    entities = [AttendanceEntity::class, AttendeeEntity::class, EventEntity::class],
    version = 1
)
abstract class AttendanceAppDatabase {
    abstract val dao: AttendanceAppDao
}