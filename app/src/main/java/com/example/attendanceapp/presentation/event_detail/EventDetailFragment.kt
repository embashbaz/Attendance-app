package com.example.attendanceapp.presentation.event_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.databinding.FragmentEventDetailBinding
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.presentation.new_attendee.NewAttendeeDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment(), NewAttendeeDialog.NewAttendeeDialogListener {

    private lateinit var eventDetailBinding: FragmentEventDetailBinding
    private val eventDetailViewModel: EventDetailViewModel by viewModels()
    private lateinit var eventDetailAdapter: EventDetailAdapter
    private var eventId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        eventDetailBinding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = eventDetailBinding.root

        if(arguments?.getParcelable<Event>("event_clicked") != null){
            eventDetailViewModel.setEventObject(requireArguments().getParcelable<Event>("event_clicked")!!)
        }
        eventDetailAdapter = EventDetailAdapter { attendee -> onAttendeeClicked(attendee) }
        setUpRecyclerView()
        collectEventDetailInfo()

        addAttendeeBtListener()

        //ToDo: replace this crap
        eventDetailBinding.stupidBt.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("event_info",requireArguments().getParcelable<Event>("event_clicked")!!)
            this.findNavController().navigate(R.id.action_eventDetailFragment_to_newAttendanceFragment, bundle)
        }


        return view
    }

    private fun addAttendeeBtListener() {
        eventDetailBinding.addAttendeeFb.setOnClickListener{
            val newAttendeeDialog = NewAttendeeDialog(eventId)
            newAttendeeDialog.setListener(this)
            newAttendeeDialog.show(parentFragmentManager, "New Attendee")
        }
    }

    fun collectEventDetailInfo(){
        collectLatestLifecycleFlow(eventDetailViewModel.eventDetailState){ data ->

            eventDetailBinding.eventNameEventDetailsTxt.setText(data.eventObject.eventName)
            eventDetailBinding.eventTypeEventDetailsTxt.setText(data.eventObject.eventType)
            eventId = data.eventObject.eventId

            if (data.participants.isNotEmpty()){
                eventDetailAdapter.setData(data.participants as ArrayList<Any>)
            }


        }
    }

    private fun setUpRecyclerView() {
        eventDetailBinding.eventDetailRecycler.layoutManager = LinearLayoutManager(activity)
        eventDetailBinding.eventDetailRecycler.adapter = eventDetailAdapter
    }

    private fun onAttendeeClicked(attendee: Any) {

    }

    override fun onDialogDismissed(value: Boolean) {
        eventDetailViewModel.getAttendees()
    }


}