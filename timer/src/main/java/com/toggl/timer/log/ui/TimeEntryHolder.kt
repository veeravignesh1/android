package com.toggl.timer.log.ui

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.toggl.common.EpoxyViewHolder
import com.toggl.timer.R
import com.toggl.timer.extensions.formatForDisplaying
import com.toggl.timer.log.domain.TimeEntryContentViewModel
import com.toggl.timer.log.domain.TimeEntryGroupViewModel
import com.toggl.timer.log.domain.formatForDisplay

class TimeEntryHolder : EpoxyViewHolder() {
    val addDescriptionLabel by bind<TextView>(R.id.add_description_label)
    val project by bind<TextView>(R.id.project_label)
    val description by bind<TextView>(R.id.description)
    val continueButton by bind<View>(R.id.continue_button)
    val billableIcon by bind<View>(R.id.billable_icon)
    val tagsIcon by bind<View>(R.id.tags)
    val groupCount by bind<TextView>(R.id.group_count)
    val duration by bind<TextView>(R.id.duration)
}

fun TimeEntryContentViewModel.bind(holder: TimeEntryHolder) {
    val hasDescription = description.isNotBlank()
    holder.addDescriptionLabel.isVisible = !hasDescription
    holder.description.isVisible = hasDescription
    holder.description.text = description
    holder.project.text = project.formatForDisplay()
    holder.billableIcon.isVisible = billable
    holder.tagsIcon.isVisible = hasTags
    holder.duration.text = duration.formatForDisplaying()
    holder.groupCount.isVisible = this is TimeEntryGroupViewModel
}