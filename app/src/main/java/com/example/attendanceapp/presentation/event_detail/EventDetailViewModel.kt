package com.example.attendanceapp.presentation.event_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import com.example.attendanceapp.domain.use_case.ValidateGetAttendeeParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getAllEventParticipant: GetAllEventParticipant,
    private val validateGetAttendeeParameters: ValidateGetAttendeeParameters
) :
    ViewModel() {

    private val _eventDetailState =
        MutableStateFlow<EventDetailFragmentState>(EventDetailFragmentState())
    val eventDetailState = _eventDetailState.asStateFlow()

    private val _eventDetailUIEvent = MutableSharedFlow<EventDetailUIEvent>(replay = 1)
    val eventDetailUIEvent = _eventDetailUIEvent

    private val _attendeepagedData = MutableSharedFlow<PagingData<Attendee>>(replay = 1)
    val attendeepagedData = _attendeepagedData.asSharedFlow()

    fun setEventObject(event: Event) {
        _eventDetailState.value = eventDetailState.value.copy(
            eventObject = event
        )
        getAttendees()
    }

    fun getAttendees() {
        viewModelScope.launch(Dispatchers.IO) {

            val eventId = _eventDetailState.value.eventObject.eventId
            val validateEventId = validateGetAttendeeParameters(eventId)

            if (validateEventId is OperationStatus.Success) {
                getAllEventParticipant(eventId).cachedIn(viewModelScope).collect { result ->

                    _attendeepagedData.emit(result)


                }
            } else if (validateEventId is OperationStatus.Error) {
                _eventDetailUIEvent.emit(
                    EventDetailUIEvent.ShowSnackBar(
                        validateEventId.message
                            ?: "An error occurred while loading participants list"
                    )
                )
            }


        }

    }

    abstract class EventDetailUIEvent {
        data class ShowSnackBar(val message: String) : EventDetailUIEvent()

    }


}