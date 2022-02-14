package com.example.attendanceapp.presentation.new_attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.attendanceapp.databinding.AttendeeItemBinding
import com.example.attendanceapp.domain.models.Attendee


class NewAttendanceAdapter(onItemCheckListener: OnItemCheckListener) :
    RecyclerView.Adapter<NewAttendanceAdapter.ViewHolder>() {


    private var allItems: List<Any> = emptyList()
    private val itemCheckListener = onItemCheckListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

        holder.bind(item, itemCheckListener)

    }

    override fun getItemCount() = allItems.size

    fun setData(items: List<Any>) {
        allItems = items
        notifyDataSetChanged()

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


}