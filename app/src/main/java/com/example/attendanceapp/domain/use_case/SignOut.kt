package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.domain.repository.AttendanceMainRepository
import javax.inject.Inject

class SignOut@Inject constructor(val repo: AttendanceMainRepository){
    suspend operator fun invoke() = repo.logout()

}