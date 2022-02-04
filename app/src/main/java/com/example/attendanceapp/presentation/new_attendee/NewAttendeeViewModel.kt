package com.example.attendanceapp.presentation.new_attendee

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.attendanceapp.core.utils.Constants
import com.example.attendanceapp.data.work.AddNewAttendeeWorker
import com.example.attendanceapp.data.work.UpdateNewAttendeeWorker
import com.example.attendanceapp.data.work.UploadImageWorker
import com.example.attendanceapp.domain.use_case.AddAttendee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class NewAttendeeViewModel @Inject constructor(private val addAttendee: AddAttendee) :
    ViewModel() {

    private val _addNewAttendeeState = MutableStateFlow(NewAttendeeDialogState())
    val addNewAttendeeState = _addNewAttendeeState.asStateFlow()

    private val _addNewAttendeeEvent = MutableSharedFlow<NewAttendeeDialogUIEvent>()
    val addNewAttendeeEvent = _addNewAttendeeEvent.asSharedFlow()

    fun createInputDataForUri(imageUri: Uri, eventId: Int, name: String): Data {
        val builder = Data.Builder()
        builder.putString(Constants.IMAGE_KEY, imageUri.toString())
        builder.putInt(Constants.EVENT_ID_KEY, eventId)
        builder.putString(Constants.NAME_KEY, name)

        return builder.build()
    }


    fun addEventAttendee(eventId: Int, name: String, imageUri: Uri) {

        val saveRecord = OneTimeWorkRequestBuilder<AddNewAttendeeWorker>()
            .setInputData(createInputDataForUri(imageUri, eventId, name))
            .build()

        val saveImage = OneTimeWorkRequestBuilder<UploadImageWorker>().build()

        val updateImageValue = OneTimeWorkRequestBuilder<UpdateNewAttendeeWorker>().build()

        WorkManager.getInstance()
            .beginWith(saveRecord)
            .then(saveImage)    // FYI, then() returns a new WorkContinuation instance
            .then(updateImageValue)
            .enqueue()


//        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
//            isLoading = true
//        )
//
//        viewModelScope.launch(Dispatchers.IO) {
//            addAttendee(eventId, name, "").collect { result ->
//                when (result) {
//                    is OperationStatus.Success -> {
//                        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
//                            isLoading = false,
//                            isSuccess = true
//                        )
//
//                        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.ShowToast("Attendee added"))
//                        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.DismissDialog(true))
//                    }
//
//                    is OperationStatus.Error -> {
//                        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
//                            isLoading = false,
//                            isError = true
//                        )
//                        _addNewAttendeeEvent.emit(
//                            NewAttendeeDialogUIEvent.ShowToast(
//                                result.message ?: "An error occurred"
//                            )
//                        )
//                    }
//                }
//
//            }
//        }


    }

    abstract class NewAttendeeDialogUIEvent {
        data class ShowToast(val message: String) : NewAttendeeDialogUIEvent()
        data class DismissDialog(val value: Boolean) : NewAttendeeDialogUIEvent()

    }


}