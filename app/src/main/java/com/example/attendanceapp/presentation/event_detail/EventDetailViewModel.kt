package com.example.attendanceapp.presentation.event_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventDetailViewModel @Inject constructor(private val getAllEventParticipant: GetAllEventParticipant) :
    ViewModel() {

    private val _eventDetailState =
        MutableStateFlow<EventDetailFragmentState>(EventDetailFragmentState())
    val eventDetailState = _eventDetailState.asStateFlow()

    private val _eventDetailUIEvent = MutableSharedFlow<EventDetailUIEvent>(replay = 1)
    val eventDetailUIEvent = _eventDetailUIEvent

    fun setEventObject(event: Event) {
        _eventDetailState.value = eventDetailState.value.copy(
            eventObject = event
        )
        getAttendees()
    }

    fun getAttendees() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllEventParticipant(_eventDetailState.value.eventObject.eventId).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _eventDetailState.value = eventDetailState.value.copy(
                            participants = result.data!!,
                            isLoading = false
                        )

                    }

                    is OperationStatus.Error -> {
                        _eventDetailUIEvent.emit(
                            EventDetailUIEvent.ShowSnackBar(
                                result.message
                                    ?: "An error occurred while loading participants list"
                            )
                        )
                    }


                }


            }


        }

    }

    abstract class EventDetailUIEvent {
        data class ShowSnackBar(val message: String) : EventDetailUIEvent()

    }


}