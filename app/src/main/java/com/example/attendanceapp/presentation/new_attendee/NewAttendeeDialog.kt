package com.example.attendanceapp.presentation.new_attendee

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.core.utils.ui.stringFromTl
import com.example.attendanceapp.databinding.NewAttendeeBinding
import com.example.attendanceapp.domain.models.Attendee
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File


@AndroidEntryPoint
class NewAttendeeDialog(private val eventId: Int, private val attendee: Attendee?) : DialogFragment() {
    private lateinit var newAttendeeDialogBinding: NewAttendeeBinding
    private val newAttendeeViewModel: NewAttendeeViewModel by viewModels()
    internal lateinit var newAttendeeDialogListener: NewAttendeeDialogListener
    val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    private var imageUri: Uri? = null

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

    }

    fun setAttendeeData(){
        if(attendee != null){
            newAttendeeDialogBinding.attendeeNameTl.editText!!.setText(attendee.name)
            if (attendee.pictureId.isNotEmpty()){
                Glide.with(newAttendeeDialogBinding.root).load(attendee.pictureId).apply(RequestOptions.circleCropTransform()).into(newAttendeeDialogBinding.attendeeImg)
            }
            newAttendeeDialogBinding.saveAttendeeBt.isEnabled = false
        }
    }

    private fun listenToImageClick() {

        newAttendeeDialogBinding.attendeeImg.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        imageUri = Uri.parse(path)

        return imageUri
    }

    fun deleteFileAfterUpload(){
        if (imageUri!= null){
            try {
              //  File(getRealPathFromUri(requireContext(),imageUri)).getAbsoluteFile().delete()
                    Log.d("IMAGE ==", imageUri.toString())
               // requireContext().contentResolver .delete(Uri.parse(getRealPathFromUri(requireContext(),imageUri)), null,null)

                //Environment.getExternal

                val fDelete = File(getRealPathFromUri(requireContext(),imageUri))
                if (fDelete.exists()) {
                    if (fDelete.delete()) {
                        MediaScannerConnection.scanFile(requireContext(), arrayOf(Environment.getExternalStorageDirectory().toString()), null) { path, uri ->
                            Log.d("debug", "DONE")
                        }
                    }
                }
            }catch (e: Exception){
                Log.e("DELETING PICTURE: ",e.toString())
            }
        }
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }


    private fun getUiDataInput() {

        if(imageBitmap != null){
            val imageUri = getImageUri(requireContext(), imageBitmap!!)
            if (imageUri != null){
                newAttendeeViewModel.addEventAttendee(eventId, stringFromTl(newAttendeeDialogBinding.attendeeNameTl), imageUri)
            }
        }else{
            newAttendeeViewModel.addEventAttendee(eventId, stringFromTl(newAttendeeDialogBinding.attendeeNameTl), null)
        }

    }

    private fun collectAddingNewAttendeeEvent() {
        collectLatestLifecycleFlow(newAttendeeViewModel.addNewAttendeeEvent){ result ->
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.DismissDialog)
                if (result.value){
                    deleteFileAfterUpload()
                    dismiss()
                }
            if (result is NewAttendeeViewModel.NewAttendeeDialogUIEvent.ShowToast){
                showLongToast(result.message)
            }
        }
    }

    private fun collectAddingNewAttendeeState(){
        collectLatestLifecycleFlow(newAttendeeViewModel.addNewAttendeeState){ result ->
            if (result.isSuccess){
                newAttendeeDialogBinding.saveAttendeeBt.isEnabled = true
            }

            if (result.isError){
                newAttendeeDialogBinding.saveAttendeeBt.isEnabled = true
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