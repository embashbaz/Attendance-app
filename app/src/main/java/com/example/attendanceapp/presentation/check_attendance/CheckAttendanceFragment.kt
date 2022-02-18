package com.example.attendanceapp.presentation.check_attendance

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.getDateFromLong
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.databinding.FragmentCheckAttendanceBinding
import com.example.attendanceapp.presentation.event_detail.GenericAttendeeAdapter
import com.example.attendanceapp.presentation.main_activity.MainActivity
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckAttendanceFragment : Fragment() {

    private val checkAttendanceViewModel: CheckAttendanceViewModel by viewModels()
    private lateinit var checkAttendanceBinding: FragmentCheckAttendanceBinding
    private lateinit var genericAttendeeAdapter: GenericAttendeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setActionBarTitle("Check attendance")
        checkAttendanceBinding = FragmentCheckAttendanceBinding.inflate(inflater, container, false)
        val view = checkAttendanceBinding.root

        genericAttendeeAdapter = GenericAttendeeAdapter { attendee -> onAttendeeClicked(attendee) }
        checkAttendanceViewModel.setEventId(requireArguments().getInt("event_id"))
        setUpRecyclerView()
        listenToQuery()
        setUpSelectQueryType()

        collectLatestUIState()
        collectLatestUIEvent()
        collectPagedData()
        return view
    }


    private fun listenToQuery() {
        checkAttendanceBinding.calIconImg.setOnClickListener {
            openDatePicker()
        }

        checkAttendanceBinding.attendeeNameSearchTl.editText!!.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        checkAttendanceViewModel.onQuery(p0.toString())
                    } else {
                        checkAttendanceViewModel.onQuery()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    private fun setUpSelectQueryType() {

        val items = listOf("All", "Query by name", "Query by date")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        (checkAttendanceBinding.queryTypeTl.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        checkAttendanceBinding.queryTypeTl.editText!!.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        if (p0.toString() == "All") {
                            checkAttendanceViewModel.setType(0)
                            checkAttendanceViewModel.onQuery("")
                        } else if (p0.toString() == "Query by name") {
                            checkAttendanceViewModel.setType(1)
                        } else if (p0.toString() == "Query by date") {
                            checkAttendanceViewModel.setType(2)
                        }

                    } else {
                        checkAttendanceViewModel.onQuery()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    private fun onAttendeeClicked(attendee: Any) {

    }

    fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(parentFragmentManager, "tag");
        datePicker.addOnPositiveButtonClickListener {
            checkAttendanceViewModel.getDate(getDateFromLong(it))
        }


    }

    fun collectLatestUIState() {
        collectLatestLifecycleFlow(checkAttendanceViewModel.attendanceState) { state ->
            if (state.type == 1) {
                checkAttendanceBinding.attendeeNameSearchTl.visibility = View.VISIBLE
                checkAttendanceBinding.calIconImg.visibility = View.GONE
            } else if (state.type == 2) {
                checkAttendanceBinding.attendeeNameSearchTl.visibility = View.GONE
                checkAttendanceBinding.calIconImg.visibility = View.VISIBLE
            } else if (state.type == 0) {
                checkAttendanceBinding.attendeeNameSearchTl.visibility = View.GONE
                checkAttendanceBinding.calIconImg.visibility = View.GONE
            }
            if (state.isLoading) {
                checkAttendanceBinding.attendanceListProgress.visibility = View.VISIBLE
            } else {
                checkAttendanceBinding.attendanceListProgress.visibility = View.INVISIBLE
            }

        }


    }

    fun collectPagedData(){
        collectLatestLifecycleFlow(checkAttendanceViewModel.attendancepagedData){
            genericAttendeeAdapter.submitData(it as PagingData<Any>)
        }
    }

    private fun collectLatestUIEvent() {
        collectLatestLifecycleFlow(checkAttendanceViewModel.attendanceUiEvent) { event ->

            if (event is CheckAttendanceViewModel.AttendanceListUIEvent.ShowSnackBar) {
                showLongSnackBar(requireView(), event.message)
            }

        }
    }

    private fun setUpRecyclerView() {
        checkAttendanceBinding.attendanceListRecycler.layoutManager = LinearLayoutManager(activity)
        checkAttendanceBinding.attendanceListRecycler.adapter = genericAttendeeAdapter
    }

}