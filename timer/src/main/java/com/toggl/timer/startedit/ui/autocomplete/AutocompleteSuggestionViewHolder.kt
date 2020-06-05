package com.toggl.timer.startedit.ui.autocomplete

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R
import com.toggl.timer.log.domain.formatForDisplay
import com.toggl.timer.startedit.ui.suggestions.SuggestionViewModel

abstract class AutocompleteSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var label: TextView = itemView.findViewById(R.id.label)
}

class TimeEntrySuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    var projectLabel: TextView = itemView.findViewById(R.id.project_label)
    var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(suggestion: SuggestionViewModel.TimeEntrySuggestion) {
        itemView.setOnClickListener { onTappedListener(suggestion.autocompleteSuggestion) }
        label.text = suggestion.description
        if (suggestion.projectViewModel == null) {
            projectDot.visibility = View.GONE
            projectLabel.visibility = View.GONE
        } else {
            projectDot.visibility = View.VISIBLE
            projectLabel.visibility = View.VISIBLE
            val projectColor = Color.parseColor(suggestion.projectViewModel.color)
            projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
            projectLabel.text = suggestion.projectViewModel.formatForDisplay(suggestion.taskName)
        }
    }
}

class ProjectSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(suggestion: SuggestionViewModel.ProjectSuggestion) {
        itemView.setOnClickListener { onTappedListener(suggestion.autocompleteSuggestion) }
        label.text = suggestion.projectViewModel.name
        val projectColor = Color.parseColor(suggestion.projectViewModel.color)
        label.setTextColor(projectColor)
        projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
    }
}

class TaskSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(suggestion: SuggestionViewModel.TaskSuggestion) {
        itemView.setOnClickListener { onTappedListener(suggestion.autocompleteSuggestion) }
        val projectColor = Color.parseColor(suggestion.projectViewModel.color)
        projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
        label.text = suggestion.projectViewModel.formatForDisplay(suggestion.taskName)
    }
}

class TagSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    fun bind(suggestion: SuggestionViewModel.TagSuggestion) {
        itemView.setOnClickListener { onTappedListener(suggestion.autocompleteSuggestion) }
        label.text = suggestion.tagName
    }
}

class CreateEntrySuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    fun bind(suggestion: SuggestionViewModel) {
        when (suggestion) {
            is SuggestionViewModel.CreateProject -> bind(
                R.string.create_project, suggestion.name, suggestion.autocompleteSuggestion
            )
            is SuggestionViewModel.CreateTag -> bind(
                R.string.create_tag, suggestion.name, suggestion.autocompleteSuggestion
            )
        }
    }

    fun bind(stringId: Int, suggestionText: String, autocompleteSuggestion: AutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion) }
        val createProjectPrefix = label.context.getText(stringId)
        label.text = "$createProjectPrefix $suggestionText"
    }
}