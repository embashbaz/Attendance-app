package com.example.attendanceapp.presentation.event_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.attendanceapp.databinding.AttendeeItemBinding
import com.example.attendanceapp.domain.models.Attendance
import com.example.attendanceapp.domain.models.Attendee

class GenericAttendeeAdapter (onClick: (Any) -> Unit) :
    RecyclerView.Adapter<GenericAttendeeAdapter.ViewHolder>(){

    private val mOnclick = onClick
    private var allItems = emptyList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

        holder.bind(item)

    }

    override fun getItemCount() = allItems.size


    fun setData(items: List<Any>) {

        allItems = items
        notifyDataSetChanged()

    }




    class ViewHolder(val attendeeItemBinding: AttendeeItemBinding, onClick: (Any) -> Unit) : RecyclerView.ViewHolder(attendeeItemBinding.root){


        fun bind(item: Any){
            if (item is Attendee) {
                attendeeItemBinding.presentCheckBox.visibility = View.INVISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.personDbId.toString())
                attendeeItemBinding.attendeeNameTxt.setText(item.name)
                if (item.pictureId.isNotBlank()){
                    Glide.with(attendeeItemBinding.root).load(item.pictureId).apply(RequestOptions.circleCropTransform()).into(attendeeItemBinding.attendeeImgListItem)
                }


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
}