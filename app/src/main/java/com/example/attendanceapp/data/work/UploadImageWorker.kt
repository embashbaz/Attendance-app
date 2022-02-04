package com.example.attendanceapp.data.work

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.attendanceapp.core.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.ByteArrayOutputStream

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

        var uploadTask = imageRef.putBytes(data).snapshot


        var progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount

        val url = imageRef.downloadUrl

        val output: Data  = workDataOf(Constants.RECORD_URL to url, Constants.RECORD_ID_KEY to recordId)

        return Result.success(output)
        // }


//        try {
//            imageRef.putBytes(data).addOnProgressListener { uploadTask ->
//                var progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
//                //makeStatusNotification("$progress % complete", appContext)
//            }
//
//
//
//        } catch (e: Exception) {
//            Log.d("WORKER: ", e.toString())
//        }
    }


}