package com.example.attendanceapp.presentation.check_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.use_case.GetAttendance
import com.example.attendanceapp.domain.use_case.ValidateGetAllParticipantParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckAttendanceViewModel @Inject constructor(private val getAttendance: GetAttendance, private val validateGetAllParticipantParameters: ValidateGetAllParticipantParameters) : ViewModel(){


    private val _attendanceState = MutableStateFlow(CheckAttendanceFragmentState())
    val attendanceState = _attendanceState.asStateFlow()

    private val _attendanceUiEvent = MutableSharedFlow<AttendanceListUIEvent>()
    val attendanceUiEvent = _attendanceUiEvent.asSharedFlow()

    private val _attendancepagedData = MutableSharedFlow<PagingData<Attendance>>(replay = 1)
    val attendancepagedData = _attendancepagedData.asSharedFlow()


        fun setType(type: Int){
            _attendanceState.value = attendanceState.value.copy(
                type = type
            )
        }

        fun setEventId(eventId: Int){
            _attendanceState.value = attendanceState.value.copy(
                eventId = eventId
            )

            onQuery()
        }

        fun getDate(date: String){
            onQuery(date)
            }

        fun onQuery(query: String = ""){
            _attendanceState.value = attendanceState.value.copy(
                isLoading = true
            )
            viewModelScope.launch(Dispatchers.IO) {
                val validateInputResult = validateGetAllParticipantParameters(_attendanceState.value.type, _attendanceState.value.eventId)

                if ( validateInputResult is OperationStatus.Success) {
                    getAttendance(
                        _attendanceState.value.type,
                        _attendanceState.value.eventId,
                        query
                    ).cachedIn(viewModelScope).collect { result ->

                        _attendanceState.value = attendanceState.value.copy(
                            isLoading = false
                        )
                        _attendancepagedData.emit(result)

//                        when (result) {
//                            is OperationStatus.Success -> {
//                                _attendanceState.value = attendanceState.value.copy(
//                                    isLoading = false,
//                                    attendance = result.data!!
//                                )
//                            }
//
//                            is OperationStatus.Error -> {
//                                _attendanceState.value = attendanceState.value.copy(
//                                    isLoading = false
//                                )
//                                _attendanceUiEvent.emit(
//                                    AttendanceListUIEvent.ShowSnackBar(
//                                        result.message ?: "An error occurred"
//                                    )
//                                )
//                            }
//
//                        }
                    }

                }else if (validateInputResult is OperationStatus.Error){
                    _attendanceState.value = attendanceState.value.copy(
                        isLoading = false
                    )
                    _attendanceUiEvent.emit(
                                    AttendanceListUIEvent.ShowSnackBar(
                                        validateInputResult.message ?: "An error occurred"
                                    )
                                )
                }
            }
        }

    sealed class AttendanceListUIEvent {
        data class ShowSnackBar(val message: String) : AttendanceListUIEvent()
    }

}