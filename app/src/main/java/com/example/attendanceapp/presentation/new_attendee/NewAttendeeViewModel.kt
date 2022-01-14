package com.example.attendanceapp.presentation.new_attendee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.AddAttendee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewAttendeeViewModel @Inject constructor(private val addAttendee: AddAttendee) :
    ViewModel() {

    private val _addNewAttendeeState = MutableStateFlow(NewAttendeeDialogState())
    val addNewAttendeeState = _addNewAttendeeState.asStateFlow()

    private val _addNewAttendeeEvent = MutableSharedFlow<NewAttendeeDialogUIEvent>()
    val addNewAttendeeEvent = _addNewAttendeeEvent.asSharedFlow()


    fun addEventAttendee(eventId: Int, name: String) {
        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            addAttendee(eventId, name, "").collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )

                        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.ShowToast("Attendee added"))
                        _addNewAttendeeEvent.emit(NewAttendeeDialogUIEvent.DismissDialog(true))
                    }

                    is OperationStatus.Error -> {
                        _addNewAttendeeState.value = addNewAttendeeState.value.copy(
                            isLoading = false,
                            isError = true
                        )
                        _addNewAttendeeEvent.emit(
                            NewAttendeeDialogUIEvent.ShowToast(
                                result.message ?: "An error occurred"
                            )
                        )
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