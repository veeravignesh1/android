package com.toggl.timer.extensions

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(@StringRes stringId: Int): String =
    itemView.context.getString(stringId)
