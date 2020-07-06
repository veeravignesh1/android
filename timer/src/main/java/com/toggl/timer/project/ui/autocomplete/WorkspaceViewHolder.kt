package com.toggl.timer.project.ui.autocomplete

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toggl.models.domain.Workspace
import com.toggl.timer.R

class WorkspaceViewHolder(
    view: View,
    val onTapListener: (Workspace) -> Unit
) : RecyclerView.ViewHolder(view) {
    var label: TextView = itemView.findViewById(R.id.label)

    fun bind(workspace: Workspace) {
        itemView.setOnClickListener { onTapListener(workspace) }
        label.text = workspace.name
    }
}