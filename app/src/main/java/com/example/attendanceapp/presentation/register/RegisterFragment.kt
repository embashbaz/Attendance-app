package com.example.attendanceapp.presentation.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var registrationBinding: FragmentRegisterBinding
    private val registrationViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registrationBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = registrationBinding.root

        listenToRegisterBtClicked()
        collectLatestUIEVent()


        return view
    }

    private fun listenToRegisterBtClicked() {
        registrationViewModel.register(
            stringFromTl(registrationBinding.emailRegisterTl),
            stringFromTl(registrationBinding.passwordRegisterTl),
            stringFromTl(registrationBinding.confirmPasswordRegisterTl)
        )
    }

    private fun collectLatestUIEVent(){
        collectLatestLifecycleFlow(registrationViewModel.registerEvent){ event ->
            if (event is RegisterViewModel.RegisterUIEvent.ShowToast){
                showLongToast(event.message)
            }

        }
    }


}