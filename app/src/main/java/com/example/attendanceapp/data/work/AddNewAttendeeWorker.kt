package com.example.attendanceapp.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.attendanceapp.core.utils.Constants
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.AddAttendee
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class AddNewAttendeeWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val addAttendee: AddAttendee
    )
    : CoroutineWorker(appContext, workerParams)  {

    override suspend fun doWork(): Result {
        val name = inputData.getString(Constants.NAME_KEY)
        val eventId = inputData.getInt(Constants.EVENT_ID_KEY, 0)
        val resourceUri = inputData.getString(Constants.IMAGE_KEY)


            var recordId = 0
            val result = addAttendee(eventId, name!!, "").first()
            if (result is OperationStatus.Success){
            recordId = result.data!!.toInt()

        }
        val output: Data = workDataOf(Constants.EVENT_ID_KEY to eventId, Constants.IMAGE_KEY to resourceUri, Constants.RECORD_ID_KEY to recordId)
        return Result.success(output)

    }
}