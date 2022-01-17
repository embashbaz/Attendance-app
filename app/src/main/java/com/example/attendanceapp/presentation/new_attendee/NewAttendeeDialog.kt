package com.example.attendanceapp.presentation.new_attendee

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewAttendeeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewAttendeeDialog(private val eventId: Int) : DialogFragment() {
    private lateinit var newAttendeeDialogBinding: NewAttendeeBinding
    private val newAttendeeViewModel: NewAttendeeViewModel by viewModels()
    internal lateinit var newAttendeeDialogListener: NewAttendeeDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        newAttendeeDialogBinding = NewAttendeeBinding.inflate(inflater, container, false)
        val view = newAttendeeDialogBinding.root
        newAttendeeDialogBinding.saveAttendeeBt.setOnClickListener {
            getUiDataInput()
        }

        return view
    }



    private fun getUiDataInput() {
        newAttendeeViewModel.addEventAttendee(eventId, stringFromTl(newAttendeeDialogBinding.attendeeNameTl))
        collectAddingNewAttendeeResults()
    }

    private fun collectAddingNewAttendeeResults() {
        collectLatestLifecycleFlow(newAttendeeViewModel.addNewAttendeeEvent){ result ->
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.DismissDialog)
                if (result.value){
                    dismiss()
                }
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.ShowToast){
                showLongToast(result.message)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        newAttendeeDialogListener.onDialogDismissed(true)
    }

    interface NewAttendeeDialogListener{
        fun onDialogDismissed(value: Boolean)
    }

    fun setListener(listener: NewAttendeeDialogListener){
        newAttendeeDialogListener = listener
    }

}