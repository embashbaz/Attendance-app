package com.example.attendanceapp

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AttendanceApp : Application(), Configuration.Provider, CameraXConfig.Provider  {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}