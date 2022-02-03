package com.example.attendanceapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.Register
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val registerUseCase: Register) : ViewModel() {

    private val _registerEvent = MutableSharedFlow<RegisterUIEvent>(replay = 1)
    val registerEvent = _registerEvent.asSharedFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            registerUseCase(email, password, confirmPassword).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _registerEvent.emit(RegisterUIEvent.ShowToast("Registration successful"))
                    }

                    is OperationStatus.Error -> {
                        _registerEvent.emit(
                            RegisterUIEvent.ShowToast(
                                result.message ?: "Registration failed"
                            )
                        )
                    }
                }
            }
        }


    }


    sealed class RegisterUIEvent {
        data class ShowToast(val message: String) : RegisterUIEvent()
    }

}