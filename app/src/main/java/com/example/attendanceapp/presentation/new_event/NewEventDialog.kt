package com.example.attendanceapp.presentation.new_event

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewEventDialogBinding
import dagger.hilt.EntryPoint

@EntryPoint
class NewEventDialog : DialogFragment() {

    lateinit var newEventDialogBinding: NewEventDialogBinding
    private val newEventViewModel: NewEventViewModel by viewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            newEventDialogBinding = NewEventDialogBinding.inflate(inflater)
            val view = newEventDialogBinding.root
            builder.setView(view)
            builder.setPositiveButton("Save event") { dialog, id ->
                getUiDataInput()
            }



            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getUiDataInput() {
        newEventViewModel.onAddEvent(
            stringFromTl(newEventDialogBinding.eventNameTl),
            stringFromTl(newEventDialogBinding.eventTypeDropdown)
        )
        collectAddingEventResult()
    }

    private fun collectAddingEventResult() {
        collectLatestLifecycleFlow(newEventViewModel.screenEvent) {
            if (it is NewEventViewModel.UIEvent.ShowToast) {
                showLongToast(it.message)
            }

            if (it is NewEventViewModel.UIEvent.dismissDialog) {
                if (it.value) {
                    dialog?.dismiss()
                }
            }
        }
    }
}