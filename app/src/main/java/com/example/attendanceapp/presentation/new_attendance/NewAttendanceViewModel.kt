package com.example.attendanceapp.presentation.new_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.AddNewAttendance
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import com.example.attendanceapp.domain.use_case.ValidateGetAttendeeParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val getAllEventParticipant: GetAllEventParticipant,
    private val addNewAttendance: AddNewAttendance,
    private val validateGetAttendeeParameters: ValidateGetAttendeeParameters
) : ViewModel() {

    private val _newAttendanceState = MutableStateFlow(NewAttendanceFragmentState())
    val newAttendanceState = _newAttendanceState.asStateFlow()

    private val _newAttendanceUIEvent = MutableSharedFlow<NewAttendanceUIEvent>(replay = 1)
    val newAttendanceUIEvent = _newAttendanceUIEvent.asSharedFlow()

    private val _attendeepagedData = MutableSharedFlow<PagingData<Attendee>>(replay = 1)
    val attendeepagedData = _attendeepagedData.asSharedFlow()

    fun setEventObject(event: Event) {
        _newAttendanceState.value = newAttendanceState.value.copy(
            eventObject = event
        )
        getAllAttendee()
    }

    fun addItemToAttendance(attendee: Attendee) {
        _newAttendanceState.value.checkedParticipants.add(attendee)
    }

    fun removeItemToAttendance(attendee: Attendee) {
        _newAttendanceState.value.checkedParticipants.remove(attendee)
    }

    fun getAllAttendee() {
        viewModelScope.launch(Dispatchers.IO) {

            val eventId = _newAttendanceState.value.eventObject.eventId
            val validateEventId = validateGetAttendeeParameters(eventId)

            if (validateEventId is OperationStatus.Success) {
                getAllEventParticipant(eventId).collect { result ->

                    _attendeepagedData.emit(result)


                }
            } else if (validateEventId is OperationStatus.Error) {
                _newAttendanceUIEvent.emit(
                    NewAttendanceUIEvent.ShowToast(
                        validateEventId.message
                            ?: "An error occurred while loading participants list"
                    )
                )
            }
        }
    }

    fun addAttendance() {
        viewModelScope.launch(Dispatchers.IO) {
            addNewAttendance(
                _newAttendanceState.value.eventObject.eventId,
                _newAttendanceState.value.eventObject.eventName,
                _newAttendanceState.value.checkedParticipants
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