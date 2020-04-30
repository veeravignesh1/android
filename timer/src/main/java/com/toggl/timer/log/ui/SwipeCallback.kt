package com.toggl.timer.log.ui

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.withTranslation
import androidx.recyclerview.widget.ItemTouchHelper
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyTouchHelper
import com.toggl.models.common.SwipeDirection

class SwipeCallback(
    private val swipeLeftParams: SwipeActionParams,
    private val swipeRightParams: SwipeActionParams,
    private val onSwipeActionListener: (model: EpoxyModel<Any>, SwipeDirection) -> Unit
) : EpoxyTouchHelper.SwipeCallbacks<EpoxyModel<Any>>() {

    private val swipeBackground = ColorDrawable(swipeLeftParams.backgroundColor)
    private val swipeLeftLabelLayout = swipeLeftParams.buildLabel()
    private val swipeRightLabelLayout = swipeRightParams.buildLabel()

    override fun onSwipeCompleted(model: EpoxyModel<Any>, itemView: View, position: Int, direction: Int) {
        onSwipeActionListener(
            model,
            if (direction == ItemTouchHelper.LEFT) SwipeDirection.Left
            else SwipeDirection.Right
        )
    }

    override fun onSwipeProgressChanged(
        model: EpoxyModel<Any>,
        itemView: View,
        swipeProgress: Float,
        c: Canvas
    ) {
        val dX = (itemView.width * swipeProgress).toInt()
        val swipingToTheRight = swipeProgress > 0
        val swipingToTheLeft = swipeProgress < 0

        with(swipeBackground) {
            when {
                swipingToTheRight -> {
                    color = swipeRightParams.backgroundColor
                    setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX,
                        itemView.bottom
                    )
                }
                swipingToTheLeft -> {
                    color = swipeLeftParams.backgroundColor
                    swipeBackground.setBounds(
                        itemView.right + dX,
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }
                else -> setBounds(0, 0, 0, 0)
            }
            draw(c)
        }

        val labelYPos = itemView.top.toFloat() + (itemView.height / 2) - (swipeRightLabelLayout.height / 2)
        when {
            swipingToTheRight -> c.withTranslation(itemView.left.toFloat() + swipeRightParams.textPadding, labelYPos) {
                swipeRightLabelLayout.draw(c)
            }
            swipingToTheLeft -> c.withTranslation(itemView.right.toFloat() - swipeLeftLabelLayout.width - swipeLeftParams.textPadding, labelYPos) {
                swipeLeftLabelLayout.draw(c)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun SwipeActionParams.buildLabel(): StaticLayout {
        val textPaint = TextPaint().apply {
            color = textColor
            textSize = fontSize
            isAntiAlias = true
        }
        return StaticLayout(
            text,
            textPaint,
            textPaint.measureText(text).toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            false
        )
    }
}

data class SwipeActionParams(
    val text: String,
    @ColorInt val backgroundColor: Int,
    @ColorInt val textColor: Int,
    val fontSize: Float,
    val textPadding: Float
)