package com.example.attendanceapp.presentation.event_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.domain.use_case.GetAllEvents
import com.example.attendanceapp.domain.use_case.SignOut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(private val getAllEventUC: GetAllEvents, private val logOut: SignOut) :
    ViewModel() {

    private val _eventListState = MutableStateFlow(EventListFragmentState())
    val eventListState = _eventListState.asStateFlow()

    private val _eventListUIEvent = MutableSharedFlow<EventListUIEvent>()
    val eventListUIEvent = _eventListUIEvent.asSharedFlow()


    fun getAllEvent() {
        _eventListState.value = eventListState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            getAllEventUC().collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _eventListState.value = eventListState.value.copy(
                            isLoading = false,
                            allEvent = result.data!!
                        )

                    }

                    is OperationStatus.Error -> {
                        _eventListState.value = eventListState.value.copy(
                            isLoading = false
                        )

                        _eventListUIEvent.emit(
                            EventListUIEvent.ShowSnackBar(
                                result.message ?: "An error occurred"
                            )
                        )

                    }


                }

            }


        }

    }

    fun signOut(){
        viewModelScope.launch(Dispatchers.IO){
            logOut().collect {  result ->
                when(result){
                    is OperationStatus.Success -> {

                        _eventListState.value = eventListState.value.copy(
                            logOut = true
                        )
                        _eventListUIEvent.emit(
                            EventListUIEvent.ShowSnackBar("signed out")
                        )

                    }

                    is OperationStatus.Error -> {
                        _eventListUIEvent.emit(
                            EventListUIEvent.ShowSnackBar(
                                result.message ?: "An error occurred"
                            )
                        )

                    }

                }
            }
        }
    }


    sealed class EventListUIEvent {
        data class eventClicked(val event: Event) : EventListUIEvent()
        data class ShowSnackBar(val message: String) : EventListUIEvent()
    }

}