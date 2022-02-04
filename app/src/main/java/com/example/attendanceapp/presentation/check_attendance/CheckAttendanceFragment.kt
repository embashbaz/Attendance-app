package com.example.attendanceapp.presentation.check_attendance

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlow
import com.example.attendanceapp.core.utils.getDateFromLong
import com.example.attendanceapp.core.utils.ui.showLongSnackBar
import com.example.attendanceapp.databinding.FragmentCheckAttendanceBinding
import com.example.attendanceapp.presentation.event_detail.GenericAttendeeAdapter
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
        checkAttendanceBinding = FragmentCheckAttendanceBinding.inflate(inflater, container, false)
        val view = checkAttendanceBinding.root

        genericAttendeeAdapter = GenericAttendeeAdapter { attendee -> onAttendeeClicked(attendee) }
        checkAttendanceViewModel.setEventId(requireArguments().getInt("event_id"))
        setUpRecyclerView()
        listenToQuery()
        onRadioButtonClicked()

        collectLatestUIState()
        collectLatestUIEvent()
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
                    }else{
                        checkAttendanceViewModel.onQuery()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    fun onRadioButtonClicked() {


        checkAttendanceBinding.radioGroupType.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.query_by_name_bt -> {
                    checkAttendanceViewModel.setType(1)
                }

                R.id.query_by_date_bt -> {
                    checkAttendanceViewModel.setType(2)
                }

                R.id.all_attendance_bt -> {
                    checkAttendanceViewModel.setType(0)
                    checkAttendanceViewModel.onQuery("")
                }

            }

        }

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

            if (state.attendance.isNotEmpty()) {
                genericAttendeeAdapter.setData(state.attendance)
            }else{
                showLongSnackBar(requireView(), "No data returned")
                genericAttendeeAdapter.setData(emptyList())
            }
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