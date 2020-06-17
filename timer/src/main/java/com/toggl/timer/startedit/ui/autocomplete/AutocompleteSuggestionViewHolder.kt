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

abstract class AutocompleteSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var label: TextView = itemView.findViewById(R.id.label)
}

class TimeEntrySuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    private var projectLabel: TextView = itemView.findViewById(R.id.project_label)
    private var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(autocompleteSuggestion: AutocompleteSuggestionViewModel.TimeEntryAutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion.autocompleteSuggestion) }
        label.text = autocompleteSuggestion.description
        if (autocompleteSuggestion.projectViewModel == null) {
            projectDot.visibility = View.GONE
            projectLabel.visibility = View.GONE
        } else {
            projectDot.visibility = View.VISIBLE
            projectLabel.visibility = View.VISIBLE
            val projectColor = Color.parseColor(autocompleteSuggestion.projectViewModel.color)
            projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
            projectLabel.text = autocompleteSuggestion.projectViewModel.formatForDisplay(autocompleteSuggestion.taskName)
        }
    }
}

class ProjectSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(autocompleteSuggestion: AutocompleteSuggestionViewModel.ProjectAutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion.autocompleteSuggestion) }
        label.text = autocompleteSuggestion.projectViewModel.name
        val projectColor = Color.parseColor(autocompleteSuggestion.projectViewModel.color)
        label.setTextColor(projectColor)
        projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
    }
}

class TaskSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    var projectDot: ImageView = itemView.findViewById(R.id.project_dot)

    fun bind(autocompleteSuggestion: AutocompleteSuggestionViewModel.TaskAutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion.autocompleteSuggestion) }
        val projectColor = Color.parseColor(autocompleteSuggestion.projectViewModel.color)
        projectDot.setColorFilter(projectColor, PorterDuff.Mode.SRC_IN)
        label.text = autocompleteSuggestion.projectViewModel.formatForDisplay(autocompleteSuggestion.taskName)
    }
}

class TagSuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    fun bind(autocompleteSuggestion: AutocompleteSuggestionViewModel.TagAutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion.autocompleteSuggestion) }
        label.text = autocompleteSuggestion.tagName
    }
}

class CreateEntrySuggestionViewHolder(
    itemView: View,
    val onTappedListener: (AutocompleteSuggestion) -> Unit = {}
) : AutocompleteSuggestionViewHolder(itemView) {
    fun bind(autocompleteSuggestion: AutocompleteSuggestionViewModel) {
        when (autocompleteSuggestion) {
            is AutocompleteSuggestionViewModel.CreateProject -> bind(
                R.string.create_project, autocompleteSuggestion.name, autocompleteSuggestion.autocompleteSuggestion
            )
            is AutocompleteSuggestionViewModel.CreateTag -> bind(
                R.string.create_tag, autocompleteSuggestion.name, autocompleteSuggestion.autocompleteSuggestion
            )
        }
    }

    fun bind(stringId: Int, suggestionText: String, autocompleteSuggestion: AutocompleteSuggestion) {
        itemView.setOnClickListener { onTappedListener(autocompleteSuggestion) }
        val createProjectPrefix = label.context.getText(stringId)
        label.text = "$createProjectPrefix $suggestionText"
    }
}