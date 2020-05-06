package com.toggl.timer.log.ui

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.toggl.timer.R
import com.toggl.timer.log.domain.TimeEntryGroupViewModel

@EpoxyModelClass
abstract class TimeEntryGroupModel : EpoxyModelWithHolder<TimeEntryHolder>() {

    @EpoxyAttribute lateinit var timeEntryGroup: TimeEntryGroupViewModel
    @EpoxyAttribute(DoNotHash) lateinit var onTappedListener: (List<Long>) -> Unit
    @EpoxyAttribute(DoNotHash) lateinit var onContinueTappedListener: (Long) -> Unit
    @EpoxyAttribute(DoNotHash) lateinit var onExpandTappedListener: (Long) -> Unit
    @JvmField @EpoxyAttribute var isSwiped: Boolean = false

    override fun getDefaultLayout(): Int = R.layout.time_entries_log_item

    override fun bind(holder: TimeEntryHolder) {
        timeEntryGroup.bind(holder)

        holder.groupCount.text = "${timeEntryGroup.timeEntryIds.size}"

        holder.continueButton.setOnClickListener {
            onContinueTappedListener(timeEntryGroup.timeEntryIds.first())
        }
        holder.groupCount.setOnClickListener {
            onExpandTappedListener(timeEntryGroup.groupId)
        }
        holder.view.setOnClickListener {
            onTappedListener(timeEntryGroup.timeEntryIds)
        }
    }
}
