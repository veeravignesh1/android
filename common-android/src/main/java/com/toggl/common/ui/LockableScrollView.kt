package com.toggl.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class LockableScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    var canScroll = true

    override fun onTouchEvent(ev: MotionEvent?) =
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> canScroll && super.onTouchEvent(ev)
            else -> super.onTouchEvent(ev)
        }

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
        canScroll && super.onInterceptTouchEvent(ev)
}