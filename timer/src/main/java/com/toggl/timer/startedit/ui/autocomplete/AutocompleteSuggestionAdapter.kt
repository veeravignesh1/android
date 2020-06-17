package com.toggl.timer.startedit.ui.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R

class AutocompleteSuggestionAdapter(
    private val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : ListAdapter<AutocompleteSuggestionViewModel, AutocompleteSuggestionViewHolder>(diffCallback) {
    private val timeEntryViewType = 0
    private val projectViewType = 1
    private val taskViewType = 2
    private val tagViewType = 3
    private val createProjectViewType = 4
    private val createTagViewType = 5

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion -> timeEntryViewType
            is AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion -> projectViewType
            is AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion -> taskViewType
            is AutocompleteSuggestionViewModel.TagAutocompleteSuggestion -> tagViewType
            is AutocompleteSuggestionViewModel.CreateProject -> createProjectViewType
            is AutocompleteSuggestionViewModel.CreateTag -> createTagViewType
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutocompleteSuggestionViewHolder {
        val layoutId = when (viewType) {
            timeEntryViewType -> R.layout.fragment_dialog_start_edit_suggestion_item
            tagViewType -> R.layout.fragment_dialog_start_edit_suggestion_tag_item
            projectViewType, taskViewType -> R.layout.fragment_dialog_start_edit_suggestion_project_item
            createProjectViewType, createTagViewType -> R.layout.fragment_dialog_start_edit_suggestion_create_item
            else -> throw IllegalStateException()
        }

        return LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
            .let { createViewHolder(viewType, it) }
    }

    private fun createViewHolder(viewType: Int, itemView: View): AutocompleteSuggestionViewHolder =
        when (viewType) {
            timeEntryViewType -> TimeEntrySuggestionViewHolder(itemView, onTappedListener)
            projectViewType -> ProjectSuggestionViewHolder(itemView, onTappedListener)
            taskViewType -> TaskSuggestionViewHolder(itemView, onTappedListener)
            tagViewType -> TagSuggestionViewHolder(itemView, onTappedListener)
            createTagViewType, createProjectViewType -> CreateEntrySuggestionViewHolder(itemView, onTappedListener)
            else -> throw IllegalStateException("Invalid Suggestion ViewHolder type")
        }

    override fun onBindViewHolder(holder: AutocompleteSuggestionViewHolder, position: Int) {
        when (holder) {
            is TimeEntrySuggestionViewHolder -> holder.bind(getItem(position) as AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion)
            is ProjectSuggestionViewHolder -> holder.bind(getItem(position) as AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion)
            is TaskSuggestionViewHolder -> holder.bind(getItem(position) as AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion)
            is TagSuggestionViewHolder -> holder.bind(getItem(position) as AutocompleteSuggestionViewModel.TagAutocompleteSuggestion)
            is CreateEntrySuggestionViewHolder -> holder.bind(getItem(position))
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<AutocompleteSuggestionViewModel>() {
            override fun areItemsTheSame(oldItem: AutocompleteSuggestionViewModel, newItem: AutocompleteSuggestionViewModel): Boolean =
                when (oldItem) {
                    is AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion && oldItem.id == newItem.id
                    is AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion && oldItem.id == newItem.id
                    is AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion && oldItem.id == newItem.id
                    is AutocompleteSuggestionViewModel.TagAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TagAutocompleteSuggestion && oldItem.id == newItem.id
                    is AutocompleteSuggestionViewModel.CreateProject -> newItem is AutocompleteSuggestionViewModel.CreateProject
                    is AutocompleteSuggestionViewModel.CreateTag -> newItem is AutocompleteSuggestionViewModel.CreateTag
                }

            override fun areContentsTheSame(oldItem: AutocompleteSuggestionViewModel, newItem: AutocompleteSuggestionViewModel): Boolean =
                when (oldItem) {
                    is AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion && oldItem == newItem
                    is AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion && oldItem == newItem
                    is AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion && oldItem == newItem
                    is AutocompleteSuggestionViewModel.TagAutocompleteSuggestion ->
                        newItem is AutocompleteSuggestionViewModel.TagAutocompleteSuggestion && oldItem == newItem
                    is AutocompleteSuggestionViewModel.CreateProject ->
                        newItem is AutocompleteSuggestionViewModel.CreateProject && oldItem.name == newItem.name
                    is AutocompleteSuggestionViewModel.CreateTag ->
                        newItem is AutocompleteSuggestionViewModel.CreateTag && oldItem.name == newItem.name
                }
        }
    }
}