package com.example.attendanceapp.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.attendanceapp.core.utils.Constants
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.core.utils.ui.makeStatusNotification
import com.example.attendanceapp.domain.use_case.UpdateRecordImage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class UpdateNewAttendeeWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val updateRecordImage: UpdateRecordImage
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val recordId = inputData.getInt(Constants.RECORD_ID_KEY, 0)
        val imageUrl = inputData.getString(Constants.RECORD_IMAGE_URL)

        try {
            val result = updateRecordImage(recordId, imageUrl ?: "").first()
            if (result is OperationStatus.Success){
               makeStatusNotification("Record added", appContext)
            }else if (result is OperationStatus.Error){
                makeStatusNotification("Error ${result.message}", appContext)
            }
        }catch (e: Exception){
            makeStatusNotification("Error $e", appContext)
        }

        return Result.success()

    }

}