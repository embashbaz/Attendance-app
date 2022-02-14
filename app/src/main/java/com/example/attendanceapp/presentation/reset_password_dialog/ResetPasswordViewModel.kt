package com.example.attendanceapp.presentation.reset_password_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.SendPasswordResetLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val sendPasswordResetLink: SendPasswordResetLink) :
    ViewModel() {

    private val _resetPasswordEvent = MutableSharedFlow<ResetPasswordUIEvent>(replay = 1)
    val resetPasswordEvent = _resetPasswordEvent.asSharedFlow()


    fun sendResetPasswordLink(email: String, confirmEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendPasswordResetLink(email, confirmEmail).collect { result ->
                when (result) {
                    is OperationStatus.Success -> {
                        _resetPasswordEvent.emit(ResetPasswordUIEvent.ShowToast("link sent"))
                    }

                    is OperationStatus.Error -> {
                        _resetPasswordEvent.emit(
                            ResetPasswordUIEvent.ShowToast(
                                result.message ?: "An error occurred"
                            )
                        )
                    }

                }

            }
        }
    }


    sealed class ResetPasswordUIEvent {
        data class ShowToast(val message: String) : ResetPasswordUIEvent()
    }


}