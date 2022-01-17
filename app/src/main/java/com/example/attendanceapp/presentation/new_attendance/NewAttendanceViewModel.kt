package com.example.attendanceapp.presentation.new_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.AddNewAttendance
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewAttendanceViewModel @Inject constructor(
    private val getAllEventParticipant: GetAllEventParticipant,
    private val addNewAttendance: AddNewAttendance
) : ViewModel() {

    private val _newAttendanceState = MutableStateFlow(NewAttendanceFragmentState())
    val newAttendanceState = _newAttendanceState.asStateFlow()

    private val _newAttendanceUIEvent = MutableSharedFlow<NewAttendanceUIEvent>(replay = 1)
    val newAttendanceUIEvent = _newAttendanceUIEvent.asSharedFlow()

    fun setEventObject(event: Event) {
        _newAttendanceState.value = newAttendanceState.value.copy(
            eventObject = event
        )
        getAllAttendee()
    }

    fun getAllAttendee() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllEventParticipant(_newAttendanceState.value.eventObject.eventId).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _newAttendanceState.value = newAttendanceState.value.copy(
                            allParticipants = result.data!!
                        )

                    }

                    is OperationStatus.Error -> {
                        _newAttendanceUIEvent.emit(
                            NewAttendanceUIEvent.ShowToast(
                                result.message
                                    ?: "An error occurred while loading participants list"
                            )
                        )
                    }
                }
            }
        }
    }

    fun addAttendance(attendees: ArrayList<Attendee>) {
        viewModelScope.launch(Dispatchers.IO) {
            addNewAttendance(
                _newAttendanceState.value.eventObject.eventId,
                _newAttendanceState.value.eventObject.eventName,
                attendees
            ).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {

                        _newAttendanceUIEvent.emit(NewAttendanceUIEvent.ShowToast("Record added"))
                        _newAttendanceUIEvent.emit(NewAttendanceUIEvent.GoBackToPreviousScreen(true))
                    }

                    is OperationStatus.Error -> {
                        _newAttendanceUIEvent.emit(
                            NewAttendanceUIEvent.ShowToast(
                                result.message
                                    ?: "An error occurred while adding attendance"
                            )
                        )

                    }

                }


            }
        }
    }


    abstract class NewAttendanceUIEvent {
        data class ShowToast(val message: String) : NewAttendanceUIEvent()
        data class GoBackToPreviousScreen(val value: Boolean) : NewAttendanceUIEvent()

    }

}