package com.toggl.calendar.contextualmenu.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.calendar.R
import com.toggl.calendar.contextualmenu.domain.ContextualMenuAction
import com.toggl.calendar.contextualmenu.domain.ContextualMenuActionViewModel

class ContextualMenuActionsAdapter(
    private val onTappedListener: (ContextualMenuAction) -> Unit
) : ListAdapter<ContextualMenuActionViewModel, ContextualMenuActionViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContextualMenuActionViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contextualmenu_action_item, parent, false)
            .let { ContextualMenuActionViewHolder(it, onTappedListener) }

    override fun onBindViewHolder(holder: ContextualMenuActionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ContextualMenuActionViewModel>() {
            override fun areItemsTheSame(
                oldItem: ContextualMenuActionViewModel,
                newItem: ContextualMenuActionViewModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ContextualMenuActionViewModel,
                newItem: ContextualMenuActionViewModel
            ): Boolean = oldItem == newItem
        }
    }
}