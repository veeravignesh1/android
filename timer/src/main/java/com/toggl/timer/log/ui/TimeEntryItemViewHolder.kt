package com.toggl.timer.log.ui

import android.view.View
import com.toggl.timer.log.domain.FlatTimeEntryViewModel

class TimeEntryItemViewHolder(
    itemView: View,
    private val onTappedListener: (Long) -> Unit,
    private val onContinueTappedListener: (Long) -> Unit
) : TimeEntryContentViewHolder<FlatTimeEntryViewModel>(itemView) {

    override fun bind(item: FlatTimeEntryViewModel) {
        super.bind(item)

        continueButton.setOnClickListener {
            onContinueTappedListener(item.id)
        }

        itemView.setOnClickListener {
            onTappedListener(item.id)
        }
    }
}
