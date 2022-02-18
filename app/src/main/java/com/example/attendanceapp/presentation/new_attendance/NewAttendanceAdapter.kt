package com.example.attendanceapp.presentation.new_attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.attendanceapp.databinding.AttendeeItemBinding
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee


class NewAttendanceAdapter(onItemCheckListener: OnItemCheckListener) :
    PagingDataAdapter<Any, NewAttendanceAdapter.ViewHolder>(ATTENDEE_COMPARATOR) {


    private var allItems: List<Any> = emptyList()
    private val itemCheckListener = onItemCheckListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as Any

        holder.bind(item, itemCheckListener)

    }


    class ViewHolder(val attendeeItemBinding: AttendeeItemBinding) :
        RecyclerView.ViewHolder(attendeeItemBinding.root) {
        fun bind(item: Any, itemCheckListener: OnItemCheckListener) {
            if (item is Attendee) {
                attendeeItemBinding.presentCheckBox.isClickable = false
                attendeeItemBinding.presentCheckBox.visibility = View.VISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.personDbId.toString())
                attendeeItemBinding.attendeeNameTxt.setText(item.name)
                if(item.pictureId.isNotEmpty())
                Glide.with(attendeeItemBinding.root).load(item.pictureId).apply(RequestOptions.circleCropTransform()).into(attendeeItemBinding.attendeeImgListItem)

            }

            handleItemPressed(item, itemCheckListener)

        }

        fun handleItemPressed(item: Any, itemCheckListener: OnItemCheckListener) {
            attendeeItemBinding.root.setOnClickListener {
                attendeeItemBinding.presentCheckBox.setChecked(
                    !attendeeItemBinding.presentCheckBox.isChecked()
                )
                if (attendeeItemBinding.presentCheckBox.isChecked()) {
                    itemCheckListener.onItemCheck(item)
                } else {
                    itemCheckListener.onItemUncheck(item)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                return ViewHolder(
                    AttendeeItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
        }


    }

    interface OnItemCheckListener {
        fun onItemCheck(item: Any)
        fun onItemUncheck(item: Any)
    }

    companion object {
        private val ATTENDEE_COMPARATOR = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is Attendee && newItem is Attendee) {
                    return oldItem.personDbId == newItem.personDbId
                } else if (oldItem is Attendance && newItem is Attendance) {
                    return oldItem.attendanceId == newItem.attendanceId
                } else {
                    return false
                }
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is Attendee && newItem is Attendee) {
                    return oldItem == newItem
                } else if (oldItem is Attendance && newItem is Attendance) {
                    return oldItem == newItem
                } else {
                    return false
                }
            }
        }
    }


}