package com.example.attendanceapp.presentation.new_attendee

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.attendanceapp.core.utils.Constants
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.data.work.AddNewAttendeeWorker
import com.example.attendanceapp.data.work.UpdateNewAttendeeWorker
import com.example.attendanceapp.data.work.UploadImageWorker
import com.example.attendanceapp.domain.use_case.AddAttendee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NewAttendeeViewModel @Inject constructor(
    private val addAttendee: AddAttendee,
    private val workManager: WorkManager
) :
    ViewModel() {

    private val _addNewAttendeeState = MutableStateFlow(NewAttendeeDialogState())
    val addNewAttendeeState = _addNewAttendeeState.asStateFlow()

    private val _addNewAttendeeEvent = MutableSharedFlow<NewAttendeeDialogUIEvent>(replay = 1)
    val addNewAttendeeEvent = _addNewAttendeeEvent.asSharedFlow()

    fun createInputDataForUri(imageUri: Uri, eventId: Int, name: String): Data {
        val builder = Data.Builder()
        builder.putString(Constants.IMAGE_KEY, imageUri.toString())
        builder.putInt(Constants.EVENT_ID_KEY, eventId)
        builder.putString(Constants.NAME_KEY, name)

        return builder.build()
    }


    fun addEventAttendee(eventId: Int, name: String, imageUri: Uri?) {

        if (imageUri != null) {
            addAttendeeWithImage(eventId, name, imageUri)
        } else {
            addAttendeeWithoutAnImage(eventId, name)

        }


    }

    fun addAttendeeWithoutAnImage(eventId: Int, name: String) {
        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            addAttendee(eventId, name, "").collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        addingAttendeeSuccessfull()
                    }

                    is OperationStatus.Error -> {
                        addingAttendeeFailed(result.message)
                    }
                }

            }
        }
    }

    fun addAttendeeWithImage(eventId: Int, name: String, imageUri: Uri?) {
        val saveRecord = OneTimeWorkRequestBuilder<AddNewAttendeeWorker>()
            .setInputData(createInputDataForUri(imageUri!!, eventId, name))
            .build()

        val saveImage = OneTimeWorkRequestBuilder<UploadImageWorker>().build()

        val updateImageValue = OneTimeWorkRequestBuilder<UpdateNewAttendeeWorker>().build()

        workManager
            .beginWith(saveRecord)
            .then(saveImage)
            .then(updateImageValue)
            .enqueue()


        getAddingNewAttendeeResult(updateImageValue)
    }

    suspend fun addingAttendeeFailed(message: String?) {
        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
            isLoading = false,
            isError = true
        )
        _addNewAttendeeEvent.emit(
            NewAttendeeDialogUIEvent.ShowToast(
                message ?: "An error occurred"
            )
        )
    }

    suspend fun addingAttendeeSuccessfull() {
        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
            isLoading = false,
            isSuccess = true
        )

        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.ShowToast("Attendee added"))
        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.DismissDialog(true))
    }


    fun getAddingNewAttendeeResult(updateImageValue: OneTimeWorkRequest) {
        workManager.getWorkInfoByIdLiveData(updateImageValue.id)
            .observeForever { info ->
                if (info != null && info.state.isFinished) {
                    val myResult = info.outputData.getString(Constants.UPDATE_IMAGE_RESULT)
                    if (myResult != null) {
                        runBlocking {
                            if (myResult == "success") {
                                addingAttendeeSuccessfull()

                            } else if (myResult.contains("Error")) {
                                addingAttendeeFailed(myResult)
                            }


                        }
                    }
                }
            }
    }

    abstract class NewAttendeeDialogUIEvent {
        data class ShowToast(val message: String) : NewAttendeeDialogUIEvent()
        data class DismissDialog(val value: Boolean) : NewAttendeeDialogUIEvent()

    }


}