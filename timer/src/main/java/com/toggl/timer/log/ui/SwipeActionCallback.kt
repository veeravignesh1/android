package com.toggl.timer.log.ui

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.core.graphics.withTranslation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.toggl.models.common.SwipeDirection

class SwipeActionCallback(
    private val swipeLeftParams: SwipeActionParams,
    private val swipeRightParams: SwipeActionParams,
    private val onSwipeActionListener: (adapterPosition: Int, SwipeDirection) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {

    private val swipeBackground = ColorDrawable(swipeLeftParams.backgroundColor)
    private val swipeLeftLabelLayout = swipeLeftParams.buildLabel()
    private val swipeRightLabelLayout = swipeRightParams.buildLabel()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = when (viewHolder) {
        is TimeEntryLogViewHolder -> ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        else -> ItemTouchHelper.ACTION_STATE_IDLE
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeActionListener(
            viewHolder.adapterPosition,
            if (direction == ItemTouchHelper.LEFT) SwipeDirection.Left
            else SwipeDirection.Right
        )
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.5f

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val swipingToTheRight = dX > 0
        val swipingToTheLeft = dX < 0

        val itemView = viewHolder.itemView
        with(swipeBackground) {
            when {
                swipingToTheRight -> {
                    color = swipeRightParams.backgroundColor
                    setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt(),
                        itemView.bottom
                    )
                }
                swipingToTheLeft -> {
                    color = swipeLeftParams.backgroundColor
                    swipeBackground.setBounds(
                        itemView.right + dX.toInt(),
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