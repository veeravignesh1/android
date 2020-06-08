package com.toggl.timer.project.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.toggl.common.extensions.adjustForUserTheme
import com.toggl.common.extensions.setBackgroundTint
import com.toggl.timer.R
import com.toggl.timer.project.domain.ColorViewModel

class ColorViewHolder(
    itemView: View,
    private val onTappedListener: (ColorViewModel) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val padlock = itemView.findViewById<ImageView>(R.id.padlock)
    private val colorCircle = itemView.findViewById<View>(R.id.color_circle)
    private val selected = itemView.findViewById<View>(R.id.selected)
    private val rainbowDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, intArrayOf(
        Color.RED, Color.YELLOW, Color.GREEN,
        Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED
    )).apply {
        shape = GradientDrawable.OVAL
        gradientType = GradientDrawable.SWEEP_GRADIENT
    }

    fun bind(item: ColorViewModel) {

        when (item) {
            is ColorViewModel.DefaultColor -> {
                colorCircle.setBackgroundResource(R.drawable.circle_shape)
                val backgroundColor = item.color.adjustForUserTheme(itemView.context)
                colorCircle.setBackgroundTint(backgroundColor)

                padlock.isVisible = false
                selected.isVisible = item.selected
            }
            ColorViewModel.PremiumLocked -> {
                colorCircle.background = rainbowDrawable
                padlock.isVisible = true
                selected.isVisible = true
            }
            is ColorViewModel.CustomColor -> {
                colorCircle.background = rainbowDrawable
                padlock.isVisible = false
                selected.isVisible = item.selected
            }
        }

        itemView.setOnClickListener {
            onTappedListener(item)
        }
    }
}