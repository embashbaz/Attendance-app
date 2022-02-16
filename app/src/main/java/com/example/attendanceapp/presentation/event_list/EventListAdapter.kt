package com.example.attendanceapp.presentation.event_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.databinding.EventItemBinding
import com.example.attendanceapp.domain.models.Event

class EventListAdapter(onClick: (Any, View, Int) -> Unit) :
    PagingDataAdapter<Event, EventListAdapter.ViewHolder>(EVENT_COMPARATOR) {

    private val mOnclick = onClick
    private var allItems = emptyList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as Any
        ViewCompat.setTransitionName(holder.itemView, "event_$position")
        holder.bind(item, position)

    }


    class ViewHolder(val eventItemBinding: EventItemBinding, onClick: (Any, View, Int) -> Unit) :
        RecyclerView.ViewHolder(eventItemBinding.root) {

        lateinit var event: Any
        var passedPosition: Int = -1

        init {
            eventItemBinding.root.setOnClickListener {
                onClick(event!!, itemView, passedPosition)
            }
        }

        fun bind(item: Any, position: Int) {
            event = item
            passedPosition = position
            if (item is Event) {
                eventItemBinding.eventNameEventListTxt.setText(item.eventName)
                eventItemBinding.eventTypeEventListTxt.setText(item.eventType)
                eventItemBinding.eventDescriptionItem.setText(item.description)
            }

        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Any, View, Int) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                return ViewHolder(EventItemBinding.inflate(layoutInflater, parent, false), onClick)
            }
        }
    }

    companion object {
        private val EVENT_COMPARATOR = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem.eventId == newItem.eventId

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem == newItem
        }
    }

}