package com.example.attendanceapp.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.FragmentLoginBinding
import com.example.attendanceapp.presentation.main_activity.MainActivity
import com.example.attendanceapp.presentation.main_activity.MainActivityViewModel
import com.example.attendanceapp.presentation.reset_password_dialog.ResetPasswordDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loginFragmentBinding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    val mainViewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginFragmentBinding =  FragmentLoginBinding.inflate(inflater,container, false)
        val view = loginFragmentBinding.root
        (activity as MainActivity).setActionBarTitle("Sign in")


        listenToLoginButtonClicked()
        listenToForgotPasswordClicked()
        collectUIEvents()
        collectUiState()
        listenToRegiterViewClicked()
        return view
    }

    private fun listenToForgotPasswordClicked() {

        loginFragmentBinding.forgotPasswordTxt.setOnClickListener {
            val dialog = ResetPasswordDialog()
            dialog.show(parentFragmentManager, "Reset Dialog")
        }
    }

    fun listenToLoginButtonClicked(){
        loginFragmentBinding.loginBt.setOnClickListener{
            loginViewModel.login(stringFromTl(loginFragmentBinding.emailLoginTl), stringFromTl(loginFragmentBinding.passwordLoginTl))

        }
    }

    fun listenToRegiterViewClicked(){
        loginFragmentBinding.moveToRegisterBt.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun collectUiState(){
        collectLatestLifecycleFlow(loginViewModel.loginState){ state ->
            if (state.isSuccess){
                mainViewModel.checkAuthStatus()
            }

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