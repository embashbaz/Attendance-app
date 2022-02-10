package com.example.attendanceapp.presentation.take_attendance_camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.GetAllEventParticipant
import com.example.attendanceapp.presentation.event_detail.EventDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraAttendanceViewModel @Inject constructor(private val getAllEventParticipant: GetAllEventParticipant) :
    ViewModel() {


    private val _cameraAttendanceState =
        MutableStateFlow<CameraAttendanceState>(CameraAttendanceState())
    val cameraAttendanceState = _cameraAttendanceState.asStateFlow()

    private val _cameraAttendanceUIEvent =
        MutableSharedFlow<EventDetailViewModel.EventDetailUIEvent>(replay = 1)
    val cameraAttendanceUIEvent = _cameraAttendanceUIEvent

    init {
        _cameraAttendanceState.value = cameraAttendanceState.value.copy(
            isDetectingFaces = true
        )
    }

    fun setEventObject(event: Event) {
        _cameraAttendanceState.value = cameraAttendanceState.value.copy(
            eventObject = event
        )
        getAttendees()
    }

    fun getAttendees() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllEventParticipant(_cameraAttendanceState.value.eventObject.eventId).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _cameraAttendanceState.value = cameraAttendanceState.value.copy(
                            participants = result.data!!,
                            isLoading = false
                        )

                    }

                    is OperationStatus.Error -> {
                        _cameraAttendanceUIEvent.emit(
                            EventDetailViewModel.EventDetailUIEvent.ShowSnackBar(
                                result.message
                                    ?: "An error occurred while loading participants list"
                            )
                        )
                    }

                }

            }

        }
    }

    sealed class CameraAttendanceEvent {
        data class ShowSnackBar(val message: String) : CameraAttendanceEvent()

    }


}