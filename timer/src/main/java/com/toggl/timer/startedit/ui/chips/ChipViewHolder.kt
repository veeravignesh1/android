package com.toggl.timer.startedit.ui.chips

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.toggl.common.extensions.adjustForUserTheme
import com.toggl.common.extensions.toColorStateList
import com.toggl.timer.R

class ChipViewHolder(
    itemView: View,
    private val onTappedListener: (ChipViewModel) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val defaultIconColor = ContextCompat.getColor(itemView.context, R.color.icon).toColorStateList()
    private val defaultBackgroundColor = ContextCompat.getColor(itemView.context, R.color.modal_card).toColorStateList()

    private val tagBackgroundColor = ContextCompat.getColor(itemView.context, R.color.tag_chip_background).toColorStateList()

    private val chip = itemView.findViewById<Chip>(R.id.chip)

    fun bind(item: ChipViewModel) {
        chip.setOnClickListener {
            onTappedListener(item)
        }

        when (item) {
            is ChipViewModel.AddTag,
            is ChipViewModel.AddProject -> {
                chip.text = item.text
                chip.chipStartPadding = 0f
                chip.isChipIconVisible = false
                chip.isCloseIconVisible = false
                chip.chipStrokeColor = defaultIconColor
                chip.chipBackgroundColor = defaultBackgroundColor
            }
            is ChipViewModel.Tag -> {
                chip.text = item.text
                chip.chipStartPadding = 0f
                chip.isChipIconVisible = false
                chip.isCloseIconVisible = true
                chip.chipStrokeColor = tagBackgroundColor
                chip.chipBackgroundColor = tagBackgroundColor
            }
            is ChipViewModel.Project -> {
                val projectColor = item.project.color.adjustForUserTheme(itemView.context)
                val backgroundColor = ColorUtils.setAlphaComponent(projectColor, 51).toColorStateList()

                chip.setChipStartPaddingResource(R.dimen.chip_padding)
                chip.chipIconTint = projectColor.toColorStateList()
                chip.text = item.project.name
                chip.isChipIconVisible = true
                chip.isCloseIconVisible = false
                chip.chipStrokeColor = Color.TRANSPARENT.toColorStateList()
                chip.chipBackgroundColor = backgroundColor
            }
        }
    }
}