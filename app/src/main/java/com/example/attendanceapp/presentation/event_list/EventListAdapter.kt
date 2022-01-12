package com.example.attendanceapp.presentation.event_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.databinding.EventItemBinding
import com.example.attendanceapp.domain.models.Event

class EventListAdapter (onClick: (Any) -> Unit) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

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



    class ViewHolder(val eventItemBinding: EventItemBinding, onClick: (Any) -> Unit) : RecyclerView.ViewHolder(eventItemBinding.root){

        fun bind(item: Any){
            if (item is Event){
                eventItemBinding.eventNameEventListTxt.setText(item.eventName)
                eventItemBinding.eventTypeEventListTxt.setText(item.eventType)
            }

        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Any ) -> Unit) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)

                return  ViewHolder(EventItemBinding.inflate(layoutInflater, parent, false), onClick)
            }
        }
    }

}