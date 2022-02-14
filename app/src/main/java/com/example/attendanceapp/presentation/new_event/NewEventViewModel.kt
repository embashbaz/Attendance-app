package com.example.attendanceapp.presentation.new_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.AddEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(private val addEvent: AddEvent) : ViewModel() {

    private val _addEventState = MutableStateFlow(NewEventDialogState())
    private val addEventState = _addEventState.asStateFlow()

    private val _screenEvent = MutableSharedFlow<UIEvent>(replay = 1)
    val screenEvent = _screenEvent.asSharedFlow()

    fun onAddEvent(eventName: String, eventType: String, description: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _addEventState.value = addEventState.value.copy(
               isLoading = true
            )
            addEvent(eventType, eventName, description).collect { result ->
                when(result){
                    is OperationStatus.Success -> {
                        _addEventState.value = addEventState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                        _screenEvent.emit(UIEvent.ShowToast("Record added"))
                        _screenEvent.emit(UIEvent.dismissDialog(true))
                    }

                    is OperationStatus.Error -> {
                        _addEventState.value = addEventState.value.copy(
                            isLoading = false,
                            isError = true
                        )
                        _screenEvent.emit(UIEvent.ShowToast("Error "+result.message))
                    }

                }
            }


        }

    }

    abstract class UIEvent {
        data class ShowToast(val message: String) : UIEvent()
        data class dismissDialog(val value: Boolean): UIEvent()

    }


}