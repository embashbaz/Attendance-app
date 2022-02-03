package com.example.attendanceapp.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var loginFragmentBinding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginFragmentBinding =  FragmentLoginBinding.inflate(inflater,container, false)
        val view = loginFragmentBinding.root


        listenToLoginButtonClicked()
        collectUIEvents()

        return view
    }

    fun listenToLoginButtonClicked(){
        loginFragmentBinding.moveToRegisterBt.setOnClickListener{
            loginViewModel.login(stringFromTl(loginFragmentBinding.emailLoginTl), stringFromTl(loginFragmentBinding.passwordLoginTl))
        }
    }

    fun collectUIEvents(){
        collectLatestLifecycleFlow(loginViewModel.loginEvent){ event ->
            if(event is LoginViewModel.LoginFragmentUIEvent.showSnackBar){
                showLongSnackBar(requireView(), event.message)
            }
        }
    }


}