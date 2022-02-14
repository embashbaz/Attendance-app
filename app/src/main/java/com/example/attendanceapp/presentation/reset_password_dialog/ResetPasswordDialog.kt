package com.example.attendanceapp.presentation.reset_password_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.ResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetPasswordDialog : DialogFragment(){

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()
    private lateinit var resetPasswordBinding: ResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = ResetPasswordBinding.inflate(inflater, container, false)
        val view = resetPasswordBinding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetPasswordBinding.resetPasswordButton.setOnClickListener {
            resetPasswordViewModel.sendResetPasswordLink(stringFromTl(resetPasswordBinding.emailResetPassword), stringFromTl(resetPasswordBinding.confirmEmailResetPassword))
            collectUIEvent()
        }
    }

    fun collectUIEvent(){
        collectLatestLifecycleFlow(resetPasswordViewModel.resetPasswordEvent){ event ->
            if (event is ResetPasswordViewModel.ResetPasswordUIEvent.ShowToast){
                showLongToast(event.message)
            }

        }
    }
}