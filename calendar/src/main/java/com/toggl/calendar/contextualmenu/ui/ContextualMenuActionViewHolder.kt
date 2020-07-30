package com.toggl.calendar.contextualmenu.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.toggl.calendar.R
import com.toggl.calendar.contextualmenu.domain.ContextualMenuAction
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.ContinueMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.CopyMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.DeleteMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.DiscardMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.EditMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.SaveMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.StartMenuActionViewModel
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel.StopMenuActionViewModel

class ContextualMenuActionViewHolder(
    itemView: View,
    private val onTappedListener: (ContextualMenuAction) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private var iconCircleOverlay: FrameLayout = itemView.findViewById(R.id.circle)
    private var icon: ImageView
    private var title: TextView

    init {
        iconCircleOverlay = itemView.findViewById(R.id.circle)
        icon = itemView.findViewById(R.id.icon)
        title = itemView.findViewById(R.id.title)
        icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.black40percent))
    }

    fun bind(item: ContextualMenuActionViewModel) {
        itemView.setOnClickListener {
            onTappedListener(item.action)
        }

        title.text = item.getString(itemView.context)
        val overlayColor = item.getColorRes(itemView.context)
        val colorFilter = PorterDuffColorFilter(overlayColor, PorterDuff.Mode.SRC_IN)
        iconCircleOverlay.background.colorFilter = colorFilter
        icon.setImageResource(item.getIconRes())
    }

    private fun ContextualMenuActionViewModel.getString(context: Context): String = context.getString(
        when (this) {
            is StartMenuActionViewModel -> R.string.start
            is StopMenuActionViewModel -> R.string.stop
            is ContinueMenuActionViewModel -> R.string.continue_this
            is SaveMenuActionViewModel -> R.string.save
            is EditMenuActionViewModel -> R.string.edit
            is DeleteMenuActionViewModel -> R.string.delete
            is DiscardMenuActionViewModel -> R.string.discard
            is CopyMenuActionViewModel -> R.string.copy_event_as_time_entry
        }
    )

    private fun ContextualMenuActionViewModel.getIconRes(): Int = when (this) {
        is StartMenuActionViewModel -> R.drawable.ic_play
        is StopMenuActionViewModel -> R.drawable.ic_stop
        is ContinueMenuActionViewModel -> R.drawable.ic_play
        is SaveMenuActionViewModel -> R.drawable.ic_check
        is EditMenuActionViewModel -> R.drawable.ic_edit_time_entry
        is DeleteMenuActionViewModel -> R.drawable.ic_delete
        is DiscardMenuActionViewModel -> R.drawable.ic_close
        is CopyMenuActionViewModel -> R.drawable.ic_copy
    }

    private fun ContextualMenuActionViewModel.getColorRes(context: Context): Int = ContextCompat.getColor(
        context,
        when (this) {
            is StartMenuActionViewModel -> R.color.calendar_contextual_menu_action_start
            is StopMenuActionViewModel -> R.color.calendar_contextual_menu_action_stop
            is ContinueMenuActionViewModel -> R.color.calendar_contextual_menu_action_continue
            is SaveMenuActionViewModel -> R.color.calendar_contextual_menu_action_save
            is EditMenuActionViewModel -> R.color.calendar_contextual_menu_action_edit
            is DeleteMenuActionViewModel -> R.color.calendar_contextual_menu_action_delete
            is DiscardMenuActionViewModel -> R.color.calendar_contextual_menu_action_discard
            is CopyMenuActionViewModel -> R.color.calendar_contextual_menu_action_copy
        }
    )
}
