package com.toggl.timer.startedit.ui

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.widget.ImageView

class BottomControlPanelAnimator(private val activeButtonColor: Int, private val inactiveButtonColor: Int) {
    private val buttonStateChangeDurationMs = 250L
    private val fullAlpha = 255
    private val noAlpha = 0

    fun animateBackground(background: Drawable, isActive: Boolean) {
        val fromAlpha = if (isActive) noAlpha else fullAlpha
        val toAlpha = if (isActive) fullAlpha else noAlpha
        ObjectAnimator.ofInt(background, "alpha", fromAlpha, toAlpha).apply {
            duration = buttonStateChangeDurationMs
            start()
        }
    }

    fun animateColorFilter(imageView: ImageView, isActive: Boolean) {
        val fromColor = if (isActive) inactiveButtonColor else activeButtonColor
        val toColor = if (isActive) activeButtonColor else inactiveButtonColor
        ObjectAnimator.ofArgb(fromColor, toColor).apply {
            duration = buttonStateChangeDurationMs
            addUpdateListener { imageView.setColorFilter(it.animatedValue as Int) }
            start()
        }
    }
}
