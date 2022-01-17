package com.example.attendanceapp.presentation.event_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.databinding.FragmentEventDetailBinding
import com.example.attendanceapp.domain.models.Event


class EventDetailFragment : Fragment() {

    private lateinit var eventDetailBinding: FragmentEventDetailBinding
    private val eventDetailViewModel: EventDetailViewModel by viewModels()
    private lateinit var eventDetailAdapter: EventDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        eventDetailBinding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = eventDetailBinding.root

        if(arguments?.getParcelable<Event>("event_clicked") != null){
            eventDetailViewModel.setEventObject(requireArguments().getParcelable<Event>("event_clicked")!!)
        }
        setUpRecyclerView()
        eventDetailAdapter = EventDetailAdapter { attendee -> onAttendeeClicked(attendee) }
        collectEventDetailInfo()

        return view
    }

    fun collectEventDetailInfo(){
        collectLatestLifecycleFlow(eventDetailViewModel.eventDetailState){ data ->

            eventDetailBinding.eventNameEventDetailsTxt.setText(data.eventObject.eventName)
            eventDetailBinding.eventNameEventDetailsTxt.setText(data.eventObject.eventType)

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


}