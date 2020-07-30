package com.toggl.timer.log.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Size
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toggl.timer.R

class SectionHeaderAdapter(
    private val sectionText: String
) : ListAdapter<Any, SectionViewHolder>(object : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = true
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = true
}) {

    var isVisible: Boolean
        get() = itemCount > 0
        set(value) {
            if (value) submitList(listOf(Any()))
            else submitList(emptyList())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.time_entries_log_section_header, parent, false)
            .let(::SectionViewHolder)

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) =
        holder.bindText(sectionText)

    override fun submitList(@Size(min = 0, max = 1) list: List<Any>?) {
        require(list != null && list.size < 2) { "Section header adapter can only have 0 or 1 item" }
        super.submitList(list)
    }
}

class SectionViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout) {
    fun bindText(item: String) {
        itemView.findViewById<TextView>(R.id.title).text = item
    }
}
