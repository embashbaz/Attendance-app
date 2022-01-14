package com.example.attendanceapp.presentation.new_attendee

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewAttendeeBinding

class NewAttendeeDialog(private val eventId: Int) : DialogFragment() {
    private lateinit var newAttendeeDialogBinding: NewAttendeeBinding
    private val newAttendeeViewModel: NewAttendeeViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            newAttendeeDialogBinding = NewAttendeeBinding.inflate(inflater)
            val view = newAttendeeDialogBinding .root
            builder.setView(view)
            builder.setPositiveButton("Save attendee") { dialog, id ->
                getUiDataInput()
            }



            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
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

}