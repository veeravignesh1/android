package com.toggl.timer.log.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toggl.timer.R

abstract class TimeEntryLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val duration: TextView = itemView.findViewById(R.id.duration)
}
