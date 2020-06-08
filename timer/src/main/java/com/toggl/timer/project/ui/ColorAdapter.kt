package com.toggl.timer.project.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.timer.R
import com.toggl.timer.project.domain.ColorViewModel

class ColorAdapter(private val onTappedListener: (ColorViewModel) -> Unit) : ListAdapter<ColorViewModel, ColorViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_dialog_project_color_item, parent, false)
            .let { ColorViewHolder(it, onTappedListener) }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ColorViewModel>() {
            override fun areItemsTheSame(
                oldItem: ColorViewModel,
                newItem: ColorViewModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ColorViewModel,
                newItem: ColorViewModel
            ): Boolean = oldItem == newItem
        }
    }
}
