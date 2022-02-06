package com.example.attendanceapp.data.work

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.attendanceapp.core.utils.Constants
import com.example.attendanceapp.core.utils.ui.makeStatusNotification
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.ByteArrayOutputStream

@HiltWorker
class UploadImageWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val storage: FirebaseStorage
) : CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {

        val eventId = inputData.getInt(Constants.EVENT_ID_KEY, 0)
        val recordId = inputData.getInt(Constants.RECORD_ID_KEY, 0)
        val resourceUri = inputData.getString(Constants.IMAGE_KEY)


        val resolver = appContext.contentResolver

        val picture = BitmapFactory.decodeStream(
            resolver.openInputStream(Uri.parse(resourceUri))
        )

        val baos = ByteArrayOutputStream()
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        var imageRef = storage.reference.child("attendance/${eventId}/${recordId}.jpg")

        var uploadTask = imageRef.putBytes(data)
        val taskResult = Tasks.await(uploadTask)

        var progress = (100.0 * taskResult.bytesTransferred) / taskResult.totalByteCount
        makeStatusNotification("upload at: $progress", appContext)

        val downloadUriTask = Tasks.await(imageRef.downloadUrl)
        val url = downloadUriTask.toString()

        val output: Data =
            workDataOf(Constants.RECORD_IMAGE_URL to url, Constants.RECORD_ID_KEY to recordId)

        return Result.success(output)


    }


}