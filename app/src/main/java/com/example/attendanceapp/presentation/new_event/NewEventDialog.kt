package com.example.attendanceapp.presentation.new_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewEventDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewEventDialog : DialogFragment() {

    lateinit var newEventDialogBinding: NewEventDialogBinding
    private val newEventViewModel: NewEventViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        newEventDialogBinding = NewEventDialogBinding.inflate(inflater, container, false)
        val view = newEventDialogBinding.root

        val items = listOf("Event", "Class")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        (newEventDialogBinding.eventTypeDropdown.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // super.onViewCreated(view, savedInstanceState)
        newEventDialogBinding.saveEventBt.setOnClickListener {
            getUiDataInput()
        }

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