package com.toggl.common.feature.domain

import androidx.core.graphics.toColorInt
import androidx.core.text.buildSpannedString
import androidx.core.text.color

data class ProjectViewModel(
    val id: Long,
    val name: String,
    val color: String,
    val clientName: String?
)

fun ProjectViewModel?.formatForDisplay(taskName: String? = null) =
    if (this == null) ""
    else
        buildSpannedString {
            color(color.toColorInt()) {
                append(name)
            }
            append(" ")
            append(clientName ?: "")
            if (taskName != null) {
                append(": $taskName")
            }
        }