package com.toggl.common.extensions

import android.app.Activity
import android.util.DisplayMetrics

fun Activity.displayMetrics(): DisplayMetrics =
    DisplayMetrics().apply { windowManager.defaultDisplay.getMetrics(this) }