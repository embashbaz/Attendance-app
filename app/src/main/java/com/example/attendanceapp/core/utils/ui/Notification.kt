package com.example.attendanceapp.core.utils.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.attendanceapp.R

const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
@JvmField
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Upload image in the background"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "show upload status"
const val CHANNEL_ID = "UPLOAD_IMAGE_NOTIFICATION"
const val NOTIFICATION_ID = 1


fun makeStatusNotification(message: String, context: Context) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("New Attendee")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOnlyAlertOnce(true)
        //.setProgress()
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}