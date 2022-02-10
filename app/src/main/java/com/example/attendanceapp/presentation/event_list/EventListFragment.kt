package com.example.attendanceapp.presentation.event_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.databinding.FragmentEventListBinding
import com.example.attendanceapp.domain.models.Event
import com.example.attendanceapp.presentation.main_activity.MainActivityViewModel
import com.example.attendanceapp.presentation.new_event.NewEventDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventListFragment : Fragment(), NewEventDialog.NewEventDialogListener {

    private lateinit var eventListFragmentBinding: FragmentEventListBinding
    private val eventListViewModel: EventListViewModel by viewModels()
    val mainViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var eventListAdapter: EventListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListFragmentBinding = FragmentEventListBinding.inflate(inflater, container, false)
        val view = eventListFragmentBinding.root
        eventListAdapter =
            EventListAdapter { item, view, position -> onEventClicked(item, view, position) }
        setUpRecyclerView()

        //getEvents()
        onNewEventFbClicked()
        mainViewModel.RUN_FOR_THE_FIRST_TIME = false
        mainViewModel.checkAuthStatus()

        return view
    }

    override fun onResume() {
        super.onResume()
        getEvents()
    }

    private fun onNewEventFbClicked() {
        eventListFragmentBinding.addEventBt.setOnClickListener {
            val newEventDialog = NewEventDialog()
            newEventDialog.setListener(this)
            newEventDialog.show(parentFragmentManager, "New Event")


        }
    }

    private fun onEventClicked(item: Any, view: View, position: Int) {

        if (item is Event) {
            val extra = FragmentNavigator.Extras.Builder()
                .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
                .build()

            val destination =
                EventListFragmentDirections.actionEventListFragmentToEventDetailFragment(
                    item,
                    position
                )
            this.findNavController().navigate(destination, extra)
        }

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
                eventListFragmentBinding.recyclerView.visibility = View.VISIBLE
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

    override fun onDismissDialog(value: Boolean) {
        getEvents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        );

    }

}