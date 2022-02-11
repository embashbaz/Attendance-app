package com.example.attendanceapp.presentation.new_attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongToast
import com.example.attendanceapp.databinding.FragmentNewAttendanceBinding
import com.example.attendanceapp.domain.models.Attendee
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.presentation.main_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewAttendanceFragment : Fragment() {

    private lateinit var newAttendanceBinding: FragmentNewAttendanceBinding
    private val newAttendanceViewModel: NewAttendanceViewModel by viewModels()
    private lateinit var newAttendanceAdapter: NewAttendanceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).setActionBarTitle("Record attendance")

        newAttendanceBinding = FragmentNewAttendanceBinding.inflate(inflater, container, false)
        val view = newAttendanceBinding.root

        setUpAdapter()
        setUpRecyclerView()

        if (arguments?.getParcelable<Event>("event_info") != null) {
            newAttendanceViewModel.setEventObject(requireArguments().getParcelable<Event>("event_info")!!)
        }

        listenToSaveAttendanceClick()
        collectAllAttendanceEventAndState()

        return view
    }

    private fun listenToSaveAttendanceClick() {
        newAttendanceBinding.saveAttendanceBt.setOnClickListener {
            newAttendanceViewModel.addAttendance()
        }
    }

    private fun setUpAdapter() {
        newAttendanceAdapter =
            NewAttendanceAdapter(object : NewAttendanceAdapter.OnItemCheckListener {
                override fun onItemCheck(item: Any) {
                    newAttendanceViewModel.addItemToAttendance(item as Attendee)

                }

                override fun onItemUncheck(item: Any) {
                    newAttendanceViewModel.removeItemToAttendance(item as Attendee)

                }


            })
    }

    private fun collectAllAttendanceEventAndState() {
        collectLatestLifecycleFlow(newAttendanceViewModel.newAttendanceState) { data ->
            if (data.allParticipants.isNotEmpty()) {
                newAttendanceAdapter.setData(data.allParticipants)
            }

            if(data.checkedParticipants.isNotEmpty()){
                newAttendanceBinding.attendeePresentTxt.text =" ${data.checkedParticipants.size} people are present"
            }
        }

        collectLatestLifecycleFlow(newAttendanceViewModel.newAttendanceUIEvent) { event ->
            if (event is NewAttendanceViewModel.NewAttendanceUIEvent.GoBackToPreviousScreen) {
                if (event.value) {
                    this.findNavController().navigateUp()
                }
            }

            if (event is NewAttendanceViewModel.NewAttendanceUIEvent.ShowToast) {
                showLongToast(event.message)
            }

        }

    }


    private fun setUpRecyclerView() {
        newAttendanceBinding.attendeeListCheckbx.layoutManager = LinearLayoutManager(activity)
        newAttendanceBinding.attendeeListCheckbx.adapter = newAttendanceAdapter
    }

}