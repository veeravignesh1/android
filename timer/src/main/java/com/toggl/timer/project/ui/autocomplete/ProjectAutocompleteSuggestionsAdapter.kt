package com.toggl.timer.project.ui.autocomplete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.models.common.AutocompleteSuggestion.ProjectSuggestions
import com.toggl.timer.R

class ProjectAutocompleteSuggestionsAdapter(
    private val onTappedListener: (ProjectSuggestions) -> Unit = {}
) :
    ListAdapter<ProjectSuggestions, ProjectAutocompleteSuggestionViewHolder>(diffCallback) {
    private val workspaceViewType = 0
    private val clientViewType = 1
    private val noClientViewType = 2
    private val createClientViewType = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAutocompleteSuggestionViewHolder {
        val layoutId = when (viewType) {
            workspaceViewType -> R.layout.fragment_dialog_project_suggestion_workspace_item
            clientViewType -> R.layout.fragment_dialog_project_suggestion_client_item
            noClientViewType -> R.layout.fragment_dialog_project_suggestion_client_item
            createClientViewType -> R.layout.fragment_dialog_start_edit_suggestion_create_item
            else -> throw IllegalStateException()
        }

        return LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
            .let { ProjectAutocompleteSuggestionViewHolder(it, onTappedListener) }
    }

    override fun onBindViewHolder(holder: ProjectAutocompleteSuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when (val item = getItem(position)) {
        is ProjectSuggestions.Workspace -> workspaceViewType
        is ProjectSuggestions.Client -> item.client?.let { clientViewType } ?: noClientViewType
        is ProjectSuggestions.CreateClient -> createClientViewType
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ProjectSuggestions>() {
            override fun areItemsTheSame(
                oldItem: ProjectSuggestions,
                newItem: ProjectSuggestions
            ): Boolean = when (oldItem) {
                is ProjectSuggestions.Workspace -> newItem is ProjectSuggestions.Workspace && oldItem.workspace.id == newItem.workspace.id
                is ProjectSuggestions.Client -> newItem is ProjectSuggestions.Client && oldItem.client?.id == newItem.client?.id
                is ProjectSuggestions.CreateClient -> newItem is ProjectSuggestions.CreateClient
            }

            override fun areContentsTheSame(
                oldItem: ProjectSuggestions,
                newItem: ProjectSuggestions
            ): Boolean = when (oldItem) {
                is ProjectSuggestions.Workspace -> newItem is ProjectSuggestions.Workspace && oldItem == newItem
                is ProjectSuggestions.Client -> newItem is ProjectSuggestions.Client && oldItem == newItem
                is ProjectSuggestions.CreateClient -> newItem is ProjectSuggestions.CreateClient && newItem == oldItem
            }
        }
    }
}