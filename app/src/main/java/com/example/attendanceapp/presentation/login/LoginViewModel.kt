package com.example.attendanceapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendanceapp.core.utils.OperationStatus
import com.example.attendanceapp.domain.use_case.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginUseCase: Login): ViewModel(){

    private val _loginState = MutableStateFlow(LoginFragmentUIState())
    val loginState = _loginState.asStateFlow()


    private val _loginEvent = MutableSharedFlow<LoginFragmentUIEvent>(replay = 1)
    val loginEvent = _loginEvent.asSharedFlow()

    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(email, password).collect {  result ->
                when(result){
                    is OperationStatus.Error -> {
                        _loginEvent.emit(LoginFragmentUIEvent.showSnackBar(result.message ?: "An error occurred"))
                    }

                    is OperationStatus.Success -> {
                        _loginState.value = loginState.value.copy(
                            isSuccess = true
                        )
                    }
                }
            }
        }
    }




    sealed class LoginFragmentUIEvent{
        data class showSnackBar(val message: String): LoginFragmentUIEvent()
    }




}