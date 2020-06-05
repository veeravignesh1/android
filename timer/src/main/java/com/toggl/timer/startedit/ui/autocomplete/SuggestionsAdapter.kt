package com.toggl.timer.startedit.ui.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R
import com.toggl.timer.startedit.ui.suggestions.SuggestionViewModel

class SuggestionsAdapter(
    private val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : ListAdapter<SuggestionViewModel, AutocompleteSuggestionViewHolder>(diffCallback) {
    private val timeEntryViewType = 0
    private val projectViewType = 1
    private val taskViewType = 2
    private val tagViewType = 3
    private val createProjectViewType = 4
    private val createTagViewType = 5

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is SuggestionViewModel.TimeEntrySuggestion -> timeEntryViewType
            is SuggestionViewModel.ProjectSuggestion -> projectViewType
            is SuggestionViewModel.TaskSuggestion -> taskViewType
            is SuggestionViewModel.TagSuggestion -> tagViewType
            is SuggestionViewModel.CreateProject -> createProjectViewType
            is SuggestionViewModel.CreateTag -> createTagViewType
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
            is TimeEntrySuggestionViewHolder -> holder.bind(getItem(position) as SuggestionViewModel.TimeEntrySuggestion)
            is ProjectSuggestionViewHolder -> holder.bind(getItem(position) as SuggestionViewModel.ProjectSuggestion)
            is TaskSuggestionViewHolder -> holder.bind(getItem(position) as SuggestionViewModel.TaskSuggestion)
            is TagSuggestionViewHolder -> holder.bind(getItem(position) as SuggestionViewModel.TagSuggestion)
            is CreateEntrySuggestionViewHolder -> holder.bind(getItem(position))
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SuggestionViewModel>() {
            override fun areItemsTheSame(oldItem: SuggestionViewModel, newItem: SuggestionViewModel): Boolean =
                when (oldItem) {
                    is SuggestionViewModel.TimeEntrySuggestion ->
                        newItem is SuggestionViewModel.TimeEntrySuggestion && oldItem.id == newItem.id
                    is SuggestionViewModel.ProjectSuggestion ->
                        newItem is SuggestionViewModel.ProjectSuggestion && oldItem.id == newItem.id
                    is SuggestionViewModel.TaskSuggestion ->
                        newItem is SuggestionViewModel.TaskSuggestion && oldItem.id == newItem.id
                    is SuggestionViewModel.TagSuggestion ->
                        newItem is SuggestionViewModel.TagSuggestion && oldItem.id == newItem.id
                    is SuggestionViewModel.CreateProject -> newItem is SuggestionViewModel.CreateProject
                    is SuggestionViewModel.CreateTag -> newItem is SuggestionViewModel.CreateTag
                }

            override fun areContentsTheSame(oldItem: SuggestionViewModel, newItem: SuggestionViewModel): Boolean =
                when (oldItem) {
                    is SuggestionViewModel.TimeEntrySuggestion ->
                        newItem is SuggestionViewModel.TimeEntrySuggestion && oldItem == newItem
                    is SuggestionViewModel.ProjectSuggestion ->
                        newItem is SuggestionViewModel.ProjectSuggestion && oldItem == newItem
                    is SuggestionViewModel.TaskSuggestion ->
                        newItem is SuggestionViewModel.TaskSuggestion && oldItem == newItem
                    is SuggestionViewModel.TagSuggestion ->
                        newItem is SuggestionViewModel.TagSuggestion && oldItem == newItem
                    is SuggestionViewModel.CreateProject ->
                        newItem is SuggestionViewModel.CreateProject && oldItem.name == newItem.name
                    is SuggestionViewModel.CreateTag ->
                        newItem is SuggestionViewModel.CreateTag && oldItem.name == newItem.name
                }
        }
    }
}