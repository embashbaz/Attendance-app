package com.example.attendanceapp.presentation.new_attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.databinding.AttendeeItemBinding
import com.example.attendanceapp.domain.models.Attendee


class NewAttendanceAdapter(val onItemCheckListener: OnItemCheckListener) :
    RecyclerView.Adapter<NewAttendanceAdapter.ViewHolder>() {


    private val allItems = ArrayList<Any>()
    val itemCheckListener = onItemCheckListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

        holder.bind(item, itemCheckListener)

    }

    override fun getItemCount() = allItems.size

    class ViewHolder(val attendeeItemBinding: AttendeeItemBinding) :
        RecyclerView.ViewHolder(attendeeItemBinding.root) {
        fun bind(item: Any, itemCheckListener: OnItemCheckListener) {
            if (item is Attendee) {
                attendeeItemBinding.presentCheckBox.visibility = View.VISIBLE
                attendeeItemBinding.attendeeIdTxt.setText(item.personDbId.toString())
                attendeeItemBinding.attendeeNameTxt.setText(item.name)

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


}