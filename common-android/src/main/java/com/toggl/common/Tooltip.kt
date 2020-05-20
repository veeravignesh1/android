package com.toggl.common

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.os.postDelayed
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.threeten.bp.Duration

fun LifecycleOwner.showTooltip(
    @StringRes textResId: Int,
    duration: Duration = Duration.ofSeconds(2)
) = BasicTooltipParams(this, textResId, duration)

fun BasicTooltipParams.above(targetView: View) =
    showTooltipFor(
        targetView,
        textResId,
        lifecycleOwner,
        duration,
        Position.Above
    )

fun BasicTooltipParams.below(targetView: View) =
    showTooltipFor(
        targetView,
        textResId,
        lifecycleOwner,
        duration,
        Position.Below
    )

fun showTooltipFor(
    targetView: View,
    @StringRes textResId: Int,
    lifecycleOwner: LifecycleOwner,
    duration: Duration = Duration.ofSeconds(2),
    position: Position = Position.Above
) {
    val res = targetView.context.resources

    val tooltipView = LayoutInflater.from(targetView.context).inflate(R.layout.layout_tooltip, null)

    val popupWindow = PopupWindow(
        tooltipView,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        false
    ).apply {
        elevation = targetView.context.resources.getDimension(R.dimen.plane_08)
        animationStyle = R.style.PopupAnimation
    }

    with(tooltipView) {
        findViewById<TextView>(R.id.tooltip_text).setText(textResId)
        measure(UNSPECIFIED, UNSPECIFIED)
    }

    val yOffset =
        if (position == Position.Above) -(targetView.height + tooltipView.measuredHeight + res.getDimensionPixelSize(R.dimen.grid_1))
        else res.getDimensionPixelSize(R.dimen.grid_1)

    val xOffset = -((tooltipView.measuredWidth - targetView.width) / 2)

    popupWindow.showAsDropDown(targetView, xOffset, yOffset)

    val h = Handler().apply {
        postDelayed(duration.toMillis(), targetView) {
            popupWindow.dismiss()
        }
    }

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            h.removeCallbacksAndMessages(targetView)
        }
    })
}

data class BasicTooltipParams(val lifecycleOwner: LifecycleOwner, @StringRes val textResId: Int, val duration: Duration)

enum class Position {
    Above,
    Below
}
