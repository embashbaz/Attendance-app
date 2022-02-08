package com.example.attendanceapp.presentation.take_attendance_camera

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.databinding.FragmentCameraAttendanceLandBinding
import com.example.attendanceapp.presentation.take_attendance_camera.camerax.CameraManager
import kotlinx.android.synthetic.main.fragment_camera_attendance_land.*


class CameraAttendanceFragment : Fragment() {

    private lateinit var fragmentCameraAttendanceBinding: FragmentCameraAttendanceLandBinding
    private lateinit var cameraManager: CameraManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        fragmentCameraAttendanceBinding = FragmentCameraAttendanceLandBinding.inflate(inflater, container, false)
        val view = fragmentCameraAttendanceBinding.root
        createCameraManager()
        checkForPermission()

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
            previewView_finder,
            this,
            graphicOverlay_finder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }


}