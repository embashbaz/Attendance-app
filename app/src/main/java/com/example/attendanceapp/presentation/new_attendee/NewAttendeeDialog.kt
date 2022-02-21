package com.example.attendanceapp.presentation.new_attendee

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.getTime
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewAttendeeBinding
import com.example.attendanceapp.domain.models.Attendee
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException


@AndroidEntryPoint
class NewAttendeeDialog(private val eventId: Int, private val attendee: Attendee?) :
    DialogFragment() {
    private lateinit var newAttendeeDialogBinding: NewAttendeeBinding
    private val newAttendeeViewModel: NewAttendeeViewModel by viewModels()
    internal lateinit var newAttendeeDialogListener: NewAttendeeDialogListener
    val REQUEST_IMAGE_CAPTURE = 1
    private var imageUri: Uri? = null
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.getWindow()?.setLayout(width, height)
        }
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
            newAttendeeDialogBinding.saveAttendeeBt.isEnabled = false
        }

        newAttendeeDialogBinding.closeNewAttendeeBt.setOnClickListener {
            showImagePath()
            // if(currentPhotoPath.isNotEmpty()){
            deleteFileAfterUpload()

            dialog?.dismiss()
        }

        listenToImageClick()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectAddingNewAttendeeEvent()
        collectAddingNewAttendeeState()
        setAttendeeData()

        showImagePath()

    }


    fun showImagePath() {
        Log.d("IMAGE PATH", currentPhotoPath)
    }

    fun setAttendeeData() {
        if (attendee != null) {
            newAttendeeDialogBinding.attendeeNameTl.editText!!.setText(attendee.name)
            if (attendee.pictureId.isNotEmpty()) {
                Glide.with(newAttendeeDialogBinding.root).load(attendee.pictureId)
                    .apply(RequestOptions.circleCropTransform())
                    .into(newAttendeeDialogBinding.attendeeImg)
            }
            newAttendeeDialogBinding.saveAttendeeBt.isEnabled = false
        }
    }

    private fun listenToImageClick() {

        newAttendeeDialogBinding.attendeeImg.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }


    private fun createImageFile(): File {
        // Create an image file name

        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            "JPEG_${getTime()}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun deleteFileAfterUpload() {

        if (currentPhotoPath.isNotEmpty())
            try {
                File(currentPhotoPath).absoluteFile.delete()
                currentPhotoPath = ""
            } catch (e: Exception) {

            }

    }


    private fun getUiDataInput() {

        if (imageUri != null) {
            newAttendeeViewModel.addEventAttendee(
                eventId,
                stringFromTl(newAttendeeDialogBinding.attendeeNameTl),
                imageUri
            )

        } else {
            newAttendeeViewModel.addEventAttendee(
                eventId,
                stringFromTl(newAttendeeDialogBinding.attendeeNameTl),
                null
            )
        }

    }

    private fun collectAddingNewAttendeeEvent() {
        collectLatestLifecycleFlow(newAttendeeViewModel.addNewAttendeeEvent) { result ->
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.DismissDialog)
                if (result.value) {
                    deleteFileAfterUpload()
                    dismiss()
                }
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.ShowToast) {
                showLongToast(result.message)
            }
        }
    }

    private fun collectAddingNewAttendeeState() {
        collectLatestLifecycleFlow(newAttendeeViewModel.addNewAttendeeState) { result ->
            if (result.isSuccess) {
                newAttendeeDialogBinding.saveAttendeeBt.isEnabled = true
            }

            if (result.isError) {
                newAttendeeDialogBinding.saveAttendeeBt.isEnabled = true
            }

        }
    }

    private fun dispatchTakePictureIntent() {

        showImagePath()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.d("IMAGE PROBLEM: ", ex.toString())
                    null
                }


                // Continue only if the File was successfully created
                photoFile?.also {
                    imageUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.attendanceapp.fileprovider",
                        it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            newAttendeeDialogBinding.attendeeImg.setImageURI(imageUri!!)
            showImagePath()


        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        newAttendeeDialogListener.onDialogDismissed(true)
    }

    interface NewAttendeeDialogListener {
        fun onDialogDismissed(value: Boolean)
    }

    fun setListener(listener: NewAttendeeDialogListener) {
        newAttendeeDialogListener = listener
    }


}