package com.example.attendanceapp.presentation.event_detail

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

class GenericAttendeeAdapter (onClick: (Any) -> Unit) :
    PagingDataAdapter<Any ,GenericAttendeeAdapter.ViewHolder>(ATTENDEE_COMPARATOR){

    private val mOnclick = onClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as Any
        holder.bind(item)

    }


    class ViewHolder(val attendeeItemBinding: AttendeeItemBinding, onClick: (Any) -> Unit) : RecyclerView.ViewHolder(attendeeItemBinding.root){

        lateinit var item: Any


        init {
           attendeeItemBinding.root.setOnClickListener {
                onClick(item)
            }
        }

        fun bind(item: Any){
            if (item is Attendee) {
                attendeeItemBinding.presentCheckBox.visibility = View.INVISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.personDbId.toString())
                attendeeItemBinding.attendeeNameTxt.setText(item.name)
                if (item.pictureId.isNotBlank()){
                    Glide.with(attendeeItemBinding.root).load(item.pictureId).apply(RequestOptions.circleCropTransform()).into(attendeeItemBinding.attendeeImgListItem)
                }
                this.item = item


            }else if(item is Attendance){
                attendeeItemBinding.presentCheckBox.visibility = View.INVISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.attendeeName)
                attendeeItemBinding.attendeeNameTxt.setText("${item.day} / ${item.time}")
                attendeeItemBinding.attendeeImgListItem.visibility = View.GONE
                attendeeItemBinding.attendeeImgListItem.layoutParams.width = 1
                attendeeItemBinding.attendeeImgListItem.layoutParams.height = 1

            }

        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Any ) -> Unit) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                return ViewHolder(
                    AttendeeItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ), onClick
                )
            }
        }
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