package com.example.attendanceapp.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.domain.use_case.CheckAuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val checkAuth: CheckAuthStatus) : ViewModel() {


    var RUN_FOR_THE_FIRST_TIME = true

    private val _authFlowState = MutableSharedFlow<AuthState>(replay = 1)
    val authFlowState = _authFlowState.asSharedFlow()

    var resultIsReady = false


    fun checkAuthStatus() {

        viewModelScope.launch(Dispatchers.Main) {
            checkAuth().collect { result ->

                if (!RUN_FOR_THE_FIRST_TIME)
                    _authFlowState.emit(AuthState.DoNothing())
                else {
                    if (result == "logged in") {
                        _authFlowState.emit(AuthState.LoggedIn())
                    } else {
                        _authFlowState.emit(AuthState.LoggedOut())
                    }

                }
                resultIsReady = true
            }
        }
    }

    sealed class AuthState {
        class LoggedIn : AuthState()
        class LoggedOut : AuthState()
        class DoNothing : AuthState()

    }
}