package com.example.attendanceapp.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
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
        var resultText = ""
        try {
            val result = updateRecordImage(recordId, imageUrl ?: "").first()

            if (result is OperationStatus.Success){
               makeStatusNotification("Record added", appContext)
                resultText = "success"
            }else if (result is OperationStatus.Error){
                resultText = "Error ${result.message}"
                makeStatusNotification(resultText, appContext)
            }
        }catch (e: Exception){
            resultText = "Error $e"
            makeStatusNotification(resultText, appContext)
        }

        val output: Data =
            workDataOf(Constants.UPDATE_IMAGE_RESULT to resultText)

        return Result.success(output)

    }

}