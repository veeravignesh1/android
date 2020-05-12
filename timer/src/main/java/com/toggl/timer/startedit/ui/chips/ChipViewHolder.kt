package com.toggl.timer.startedit.ui.chips

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.toggl.timer.R

class ChipViewHolder(
    itemView: View,
    private val onTappedListener: (ChipViewModel) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val chip = itemView.findViewById<Chip>(R.id.chip)

    fun bind(item: ChipViewModel) {
        chip.text = item.text
        chip.setOnClickListener {
            onTappedListener(item)
        }
    }
}