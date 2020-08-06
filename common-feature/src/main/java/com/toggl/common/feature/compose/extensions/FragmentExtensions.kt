package com.toggl.common.feature.compose.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.compose.state
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.ui.core.ViewAmbient
import androidx.ui.core.setContent
import androidx.ui.unit.Dp
import androidx.ui.unit.dp

fun Fragment.createComposeView(
    callback: @Composable (statusBarHeight: Dp, navigationBarHeight: Dp) -> Unit
): ViewGroup = blankFrameLayout().apply {
    setContent(Recomposer.current()) {

        var topInsets by state { 0.dp }
        var bottomInsets by state { 0.dp }

        val view = ViewAmbient.current

        onCommit(view) {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                topInsets = insets.systemWindowInsetTop.pixelsToDp(context).dp
                bottomInsets = insets.systemWindowInsetBottom.pixelsToDp(context).dp
                insets
            }


            val attachListener = object : View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(v: View) = Unit
                override fun onViewAttachedToWindow(v: View) { v.requestApplyInsets() }
            }
            view.addOnAttachStateChangeListener(attachListener)

            if (view.isAttachedToWindow) {
                view.requestApplyInsets()
            }

            onDispose {
                this@apply.setOnApplyWindowInsetsListener(null)
            }
        }

        callback(topInsets, bottomInsets)
    }
}

fun Fragment.createComposeFullscreenView(
    content: @Composable () -> Unit
): ViewGroup = blankFrameLayout().apply {
    setContent(Recomposer.current()) {
        content.invoke()
    }
}

private fun Fragment.blankFrameLayout() = FrameLayout(requireContext()).apply {
    isClickable = false
    isFocusable = false
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
}

fun Int.pixelsToDp(context: Context): Int =
    (this / context.resources.displayMetrics.density).toInt()
