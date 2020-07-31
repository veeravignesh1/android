package com.toggl.timer.startedit.ui.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.CreateProject
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.CreateTag
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.TagAutocompleteSuggestion
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion

class AutocompleteSuggestionAdapter(
    private val onTappedListener: (AutocompleteSuggestion.StartEditSuggestions) -> Unit = {}
) : ListAdapter<AutocompleteSuggestionViewModel, AutocompleteSuggestionViewHolder>(diffCallback) {
    private val timeEntryViewType = 0
    private val projectViewType = 1
    private val taskViewType = 2
    private val tagViewType = 3
    private val createProjectViewType = 4
    private val createTagViewType = 5

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TimeEntryAutocompleteSuggestion -> timeEntryViewType
            is ProjectAutocompleteSuggestion -> projectViewType
            is TaskAutocompleteSuggestion -> taskViewType
            is TagAutocompleteSuggestion -> tagViewType
            is CreateProject -> createProjectViewType
            is CreateTag -> createTagViewType
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
            is TimeEntrySuggestionViewHolder -> holder.bind(getItem(position) as TimeEntryAutocompleteSuggestion)
            is ProjectSuggestionViewHolder -> holder.bind(getItem(position) as ProjectAutocompleteSuggestion)
            is TaskSuggestionViewHolder -> holder.bind(getItem(position) as TaskAutocompleteSuggestion)
            is TagSuggestionViewHolder -> holder.bind(getItem(position) as TagAutocompleteSuggestion)
            is CreateEntrySuggestionViewHolder -> holder.bind(getItem(position))
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<AutocompleteSuggestionViewModel>() {
            override fun areItemsTheSame(oldItem: AutocompleteSuggestionViewModel, newItem: AutocompleteSuggestionViewModel): Boolean =
                when (oldItem) {
                    is TimeEntryAutocompleteSuggestion -> newItem is TimeEntryAutocompleteSuggestion && oldItem.id == newItem.id
                    is ProjectAutocompleteSuggestion -> newItem is ProjectAutocompleteSuggestion && oldItem.id == newItem.id
                    is TaskAutocompleteSuggestion -> newItem is TaskAutocompleteSuggestion && oldItem.id == newItem.id
                    is TagAutocompleteSuggestion -> newItem is TagAutocompleteSuggestion && oldItem.id == newItem.id
                    is CreateProject -> newItem is CreateProject
                    is CreateTag -> newItem is CreateTag
                }

            override fun areContentsTheSame(oldItem: AutocompleteSuggestionViewModel, newItem: AutocompleteSuggestionViewModel): Boolean =
                when (oldItem) {
                    is TimeEntryAutocompleteSuggestion -> newItem is TimeEntryAutocompleteSuggestion && oldItem == newItem
                    is ProjectAutocompleteSuggestion -> newItem is ProjectAutocompleteSuggestion && oldItem == newItem
                    is TaskAutocompleteSuggestion -> newItem is TaskAutocompleteSuggestion && oldItem == newItem
                    is TagAutocompleteSuggestion -> newItem is TagAutocompleteSuggestion && oldItem == newItem
                    is CreateProject -> newItem is CreateProject && oldItem.name == newItem.name
                    is CreateTag -> newItem is CreateTag && oldItem.name == newItem.name
                }
        }
    }
}
