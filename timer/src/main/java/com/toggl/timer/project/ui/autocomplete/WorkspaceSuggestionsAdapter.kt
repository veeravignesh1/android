package com.toggl.timer.project.ui.autocomplete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.models.domain.Workspace
import com.toggl.timer.R

class WorkspaceSuggestionsAdapter(private val onWorkspaceSelected: (Workspace) -> Unit) :
    ListAdapter<Workspace, WorkspaceViewHolder>(object : DiffUtil.ItemCallback<Workspace>() {
        override fun areItemsTheSame(oldItem: Workspace, newItem: Workspace): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Workspace, newItem: Workspace): Boolean = oldItem.name == newItem.name
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkspaceViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_dialog_project_suggestion_workspace_item, parent, false)
            .let { WorkspaceViewHolder(it, onWorkspaceSelected) }

    override fun onBindViewHolder(holder: WorkspaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}