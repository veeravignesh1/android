package com.toggl.timer.log.ui

import android.view.View
import com.toggl.timer.log.domain.TimeEntryGroupViewModel

class TimeEntryGroupViewHolder(
    itemView: View,
    private val onTappedListener: (List<Long>) -> Unit,
    private val onContinueTappedListener: (Long) -> Unit,
    private val onExpandTappedListener: (Long) -> Unit
) : TimeEntryContentViewHolder<TimeEntryGroupViewModel>(itemView) {

    override fun bind(item: TimeEntryGroupViewModel) {
        super.bind(item)

        groupCount.text = "${item.timeEntryIds.size}"

        continueButton.setOnClickListener {
            onContinueTappedListener(item.timeEntryIds.first())
        }
        groupCount.setOnClickListener {
            onExpandTappedListener(item.groupId)
        }
        itemView.setOnClickListener {
            onTappedListener(item.timeEntryIds)
        }
    }
}
