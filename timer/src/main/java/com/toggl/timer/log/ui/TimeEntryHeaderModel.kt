package com.toggl.timer.log.ui

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.toggl.common.EpoxyViewHolder
import com.toggl.timer.R
import com.toggl.timer.extensions.formatForDisplaying
import org.threeten.bp.Duration

@EpoxyModelClass
abstract class TimeEntryHeaderModel : EpoxyModelWithHolder<HeaderHolder>() {

    override fun getDefaultLayout(): Int = R.layout.time_entries_log_header

    @EpoxyAttribute lateinit var text: CharSequence
    @EpoxyAttribute lateinit var duration: Duration

    override fun bind(holder: HeaderHolder) {
        holder.titleLabel.text = text
        holder.durationLabel.text = duration.formatForDisplaying()
    }
}

class HeaderHolder : EpoxyViewHolder() {
    val titleLabel by bind<TextView>(R.id.title)
    val durationLabel by bind<TextView>(R.id.duration)
}
