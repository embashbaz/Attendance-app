package com.example.attendanceapp.presentation.event_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.databinding.FragmentEventListBinding
import com.example.attendanceapp.presentation.new_event.NewEventDialog

class EventListFragment : Fragment() {

    private lateinit var eventListFragmentBinding: FragmentEventListBinding
    private val eventListViewModel: EventListViewModel by viewModels()
    private lateinit var eventListAdapter: EventListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListFragmentBinding = FragmentEventListBinding.inflate(inflater, container, false)
        val view = eventListFragmentBinding.root
        eventListAdapter = EventListAdapter { item -> onEventClicked(item) }
        setUpRecyclerView()

        getEvents()
        onNewEventFbClicked()

        return view
    }

    private fun onNewEventFbClicked() {
        eventListFragmentBinding.addEventBt.setOnClickListener{
            val newEventDialog = NewEventDialog()
            newEventDialog.show(parentFragmentManager, "New Event")


        }
    }

    private fun onEventClicked(item: Any) {

    }

    private fun getEvents() {
        eventListViewModel.getAllEvent()
        collectEventsStates()
        collectUIEvents()
    }

    private fun collectEventsStates() {
        collectLatestLifecycleFlow(eventListViewModel.eventListState) { state ->
            if (state.isLoading) {
                eventListFragmentBinding.eventListProgres.visibility = View.VISIBLE
            } else {
                eventListFragmentBinding.eventListProgres.visibility = View.INVISIBLE
            }

            if (state.allEvent.isNotEmpty()) {
                eventListFragmentBinding.recyclerView.visibility = View.INVISIBLE
                eventListAdapter.setData(state.allEvent)

            }

        }
    }

    private fun collectUIEvents() {
        collectLatestLifecycleFlow(eventListViewModel.eventListUIEvent) { event ->
            if (event is EventListViewModel.EventListUIEvent.ShowSnackBar) {
                showLongSnackBar(requireView(), event.message)
            }
        }


    }

    private fun setUpRecyclerView() {
        eventListFragmentBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        eventListFragmentBinding.recyclerView.adapter = eventListAdapter
    }

}