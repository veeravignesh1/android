package com.toggl.timer.log.ui

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.toggl.timer.R
import com.toggl.timer.log.domain.FlatTimeEntryViewModel

@EpoxyModelClass
abstract class TimeEntryItemModel : EpoxyModelWithHolder<TimeEntryHolder>() {

    @EpoxyAttribute lateinit var timeEntry: FlatTimeEntryViewModel
    @EpoxyAttribute(DoNotHash) lateinit var onTappedListener: (Long) -> Unit
    @EpoxyAttribute(DoNotHash) lateinit var onContinueTappedListener: (Long) -> Unit

    override fun getDefaultLayout(): Int = R.layout.time_entries_log_item

    override fun bind(holder: TimeEntryHolder) {
        timeEntry.bind(holder)

        holder.continueButton.setOnClickListener {
            onContinueTappedListener(timeEntry.id)
        }
        holder.view.setOnClickListener {
            onTappedListener(timeEntry.id)
        }
    }
}

