package com.example.attendanceapp.presentation.check_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.GetAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckAttendanceViewModel @Inject constructor(private val getAttendance: GetAttendance) : ViewModel(){


    private val _attendanceState = MutableStateFlow(CheckAttendanceFragmentState())
    val attendanceState = _attendanceState.asStateFlow()

    private val _attendanceUiEvent = MutableSharedFlow<AttendanceListUIEvent>(replay = 1)
    val attendanceUiEvent = _attendanceUiEvent.asSharedFlow()


        fun setType(type: Int){
            _attendanceState.value = attendanceState.value.copy(
                type = type
            )
        }

        fun setEventId(eventId: Int){
            _attendanceState.value = attendanceState.value.copy(
                eventId = eventId
            )

            onQuery("")
        }

        fun getDate(date: String){
            onQuery(date)
            }

        fun onQuery(query: String){
            _attendanceState.value = attendanceState.value.copy(
                isLoading = true
            )
            viewModelScope.launch(Dispatchers.IO) {
                getAttendance(_attendanceState.value.type, _attendanceState.value.eventId, query).collect { result ->
                        when(result){
                            is OperationStatus.Success -> {
                                _attendanceState.value = attendanceState.value.copy(
                                    isLoading = false,
                                    attendance = result.data!!
                                )
                            }

                            is OperationStatus.Error -> {
                                _attendanceState.value = attendanceState.value.copy(
                                    isLoading = false
                                )
                                _attendanceUiEvent.emit(AttendanceListUIEvent.ShowSnackBar(result.message ?: "An error occurred"))
                            }

                        }
                }

            }
        }

    sealed class AttendanceListUIEvent {
        data class ShowSnackBar(val message: String) : AttendanceListUIEvent()
    }

}