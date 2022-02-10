package com.example.attendanceapp.presentation.take_attendance_camera

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.databinding.FragmentCameraAttendanceBinding
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.presentation.take_attendance_camera.camerax.CameraManager
import com.example.attendanceapp.presentation.take_attendance_camera.face_detection.FaceContourDetectionProcessor
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


@AndroidEntryPoint
class CameraAttendanceFragment : Fragment(), FaceContourDetectionProcessor.MostFacesListener {

    private lateinit var fragmentCameraAttendanceBinding: FragmentCameraAttendanceBinding
    private lateinit var cameraManager: CameraManager
    private val functions = Firebase.functions
    private  var allParticipant = emptyList<Attendee>()

    private val cameraAttendanceViewModel: CameraAttendanceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentCameraAttendanceBinding = FragmentCameraAttendanceBinding.inflate(inflater, container, false)
        val view = fragmentCameraAttendanceBinding.root
        if(arguments?.getParcelable<Event>("event_passed") != null){
            cameraAttendanceViewModel.setEventObject(requireArguments().getParcelable<Event>("passed")!!)
        }
        createCameraManager()
        checkForPermission()

        collectLatestCameraState()

        // Inflate the layout for this fragment
        return view
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {

                showLongToast("Permissions not granted by the user.")
                //finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            requireContext(),
            fragmentCameraAttendanceBinding.previewViewFinder!!,
            this,
            fragmentCameraAttendanceBinding.graphicOverlayFinder!!,
            this


        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun collectLatestCameraState(){
        collectLatestLifecycleFlow(cameraAttendanceViewModel.cameraAttendanceState){ state ->
            allParticipant = state.participants
        }
    }



    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    @SuppressLint("SetTextI18n")
    override fun numberOfFaces(num: Int) {
        Log.d("NUMBER OF ATTENDEE: ", num.toString())
    //  showLongToast("number of people $num")
        fragmentCameraAttendanceBinding.textView.setText("number of people $num")
    }

    override fun imageMostPeople(image: InputImage) {
        val imageBit = toBitmap(image.mediaImage!!)
        fragmentCameraAttendanceBinding.button.setOnClickListener {
            Log.d("Button clicked ", "Button clicked")
           // if (imageBit != null){

                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBit!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                Log.d("IMAGE.... ", encoded)

                addMessage(encoded)


           // }
        }


    }

    private fun toBitmap(image: Image?): Bitmap? {
        val planes: Array<Image.Plane> = image?.planes ?: return null
        val yBuffer: ByteBuffer = planes[0].buffer
        val uBuffer: ByteBuffer = planes[1].buffer
        val vBuffer: ByteBuffer = planes[2].buffer
        val ySize: Int = yBuffer.remaining()
        val uSize: Int = uBuffer.remaining()
        val vSize: Int = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes: ByteArray = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun addMessage(image: String): Task<String> {


        var attendeeIds = ""
        var attendeesUrl = ""
        for ((index, attendee) in allParticipant.withIndex()){
            if (index < allParticipant.size - 1) {
                attendeeIds = attendeeIds + attendee.personDbId + "#"
                attendeesUrl = attendeesUrl + attendee.pictureId + "#"
            }else{
                attendeeIds += attendee.personDbId
                attendeesUrl += attendee.pictureId
            }
        }
        val data = hashMapOf(
            "imageString" to image,
            "attenddeesId" to attendeeIds,
            "attendeePhotoUrls" to attendeesUrl,
            "push" to true
        )

        return functions
            .getHttpsCallable("identifyPeople")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                Log.d("People in picture: ", result)
                result
            }
    }




}