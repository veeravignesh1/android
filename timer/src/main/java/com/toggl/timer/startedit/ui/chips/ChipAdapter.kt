package com.toggl.timer.startedit.ui.chips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.timer.R

class ChipAdapter(private val onTappedListener: (ChipViewModel) -> Unit) : ListAdapter<ChipViewModel, ChipViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_dialog_start_edit_chip, parent, false)
            .let { ChipViewHolder(it, onTappedListener) }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ChipViewModel>() {
            override fun areItemsTheSame(
                oldItem: ChipViewModel,
                newItem: ChipViewModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ChipViewModel,
                newItem: ChipViewModel
            ): Boolean = oldItem.text == newItem.text
        }
    }
}
