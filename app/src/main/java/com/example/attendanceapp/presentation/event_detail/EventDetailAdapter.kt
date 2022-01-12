package com.example.attendanceapp.presentation.event_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.databinding.AttendeeItemBinding
import com.example.attendanceapp.domain.models.Attendee

class EventDetailAdapter (onClick: (Any) -> Unit) :
    RecyclerView.Adapter<EventDetailAdapter.ViewHolder>(){

    private val mOnclick = onClick
    private val allItems = ArrayList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

        holder.bind(item)

    }

    override fun getItemCount() = allItems.size


    fun setData(items: ArrayList<Any>) {
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }




    class ViewHolder(val attendeeItemBinding: AttendeeItemBinding, onClick: (Any) -> Unit) : RecyclerView.ViewHolder(attendeeItemBinding.root){


        fun bind(item: Any){
            if (item is Attendee) {
                attendeeItemBinding.presentCheckBox.visibility = View.INVISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.personDbId.toString())
                attendeeItemBinding.attendeeNameTxt.setText(item.name)

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