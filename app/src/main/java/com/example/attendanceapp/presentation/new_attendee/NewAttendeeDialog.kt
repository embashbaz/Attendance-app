package com.example.attendanceapp.presentation.new_attendee

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
    val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null

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

        listenToImageClick()

        return view
    }

    private fun listenToImageClick() {
        newAttendeeDialogBinding.attendeeImg.setOnClickListener {
            dispatchTakePictureIntent()
        }
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

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            activity?.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            newAttendeeDialogBinding.attendeeImg.setImageBitmap(imageBitmap)
            //confirmSavingPicture(imageBitmap!!)

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