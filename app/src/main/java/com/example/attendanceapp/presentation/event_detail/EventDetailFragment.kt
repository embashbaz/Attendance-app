package com.example.attendanceapp.presentation.event_detail

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.databinding.FragmentEventDetailBinding
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.presentation.main_activity.MainActivity
import com.example.attendanceapp.presentation.new_attendee.NewAttendeeDialog
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment(), NewAttendeeDialog.NewAttendeeDialogListener {

    private lateinit var eventDetailBinding: FragmentEventDetailBinding
    private val eventDetailViewModel: EventDetailViewModel by viewModels()
    private lateinit var genericAttendeeAdapter: GenericAttendeeAdapter
    private lateinit var eventObject: Event


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        (activity as MainActivity).setActionBarTitle("Event detail")

        eventDetailBinding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = eventDetailBinding.root

        val position =EventDetailFragmentArgs.fromBundle(requireArguments()).position
        ViewCompat.setTransitionName(eventDetailBinding.cardViewEventDetails, "event_${position}")
        eventDetailViewModel.setEventObject(EventDetailFragmentArgs.fromBundle(requireArguments()).event)
        collectEventDetailInfo()

        genericAttendeeAdapter = GenericAttendeeAdapter { attendee -> onAttendeeClicked(attendee) }
        setUpRecyclerView()


        addAttendeeBtListener()

        return view
    }

    private fun addAttendeeBtListener() {
        eventDetailBinding.addAttendeeFb.setOnClickListener{
            val newAttendeeDialog = NewAttendeeDialog(eventObject.eventId)
            newAttendeeDialog.setListener(this)
            newAttendeeDialog.show(parentFragmentManager, "New Attendee")
        }
    }

    fun collectEventDetailInfo(){
        collectLatestLifecycleFlow(eventDetailViewModel.eventDetailState){ data ->

            eventDetailBinding.eventNameEventDetailsTxt.setText(data.eventObject.eventName)
            eventDetailBinding.eventTypeEventDetailsTxt.setText(data.eventObject.eventType)
            eventDetailBinding.eventDescriptionDetail.setText(data.eventObject.description)
            eventObject = data.eventObject

            if (data.participants.isNotEmpty()){
                genericAttendeeAdapter.setData(data.participants)
            }


        }
    }

    private fun setUpRecyclerView() {
        eventDetailBinding.eventDetailRecycler.layoutManager = LinearLayoutManager(activity)
        eventDetailBinding.eventDetailRecycler.adapter = genericAttendeeAdapter
    }

    private fun onAttendeeClicked(attendee: Any) {

    }

    override fun onDialogDismissed(value: Boolean) {
        eventDetailViewModel.getAttendees()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.event_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId == R.id.new_attend_menu){
           navigateToRecordAttendance()
       }else if (item.itemId == R.id.check_att_menu){
           navigateToCheckAttendance()
       }else if (item.itemId == R.id.take_attend_cam_menu){
           navigateToCameraAttendance()
       }


        return super.onOptionsItemSelected(item)
    }

    fun navigateToCheckAttendance(){
        val bundle = Bundle()
        bundle.putInt("event_id",eventObject.eventId)
        this.findNavController().navigate(R.id.action_eventDetailFragment_to_checkAttendanceFragment, bundle)

    }

    fun navigateToCameraAttendance(){
        this.findNavController().navigate(R.id.action_eventDetailFragment_to_cameraAttendanceFragment)
    }

    fun navigateToRecordAttendance(){
        val bundle = Bundle()
        bundle.putParcelable("event_info",eventObject)
        this.findNavController().navigate(R.id.action_eventDetailFragment_to_newAttendanceFragment, bundle)

    }




}