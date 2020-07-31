package com.toggl.settings.compose.extensions

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.fragment.app.Fragment
import androidx.ui.core.setContent
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.common.extensions.doOnInsetsChanged

fun Fragment.createComposeView(
    callback: ViewGroup.(statusBarHeight: Dp, navigationBarHeight: Dp) -> Unit
): ViewGroup = blankFrameLayout().apply {
    doOnInsetsChanged { statusBarHeight, navigationBarHeight ->
        callback(statusBarHeight.pixelsToDp(context).dp, navigationBarHeight.pixelsToDp(context).dp)
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
