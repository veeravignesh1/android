package com.toggl.timer.project.ui.autocomplete

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R
import com.toggl.timer.extensions.getString

class ProjectAutocompleteSuggestionViewHolder(
    view: View,
    val onTappedListener: (AutocompleteSuggestion.ProjectSuggestions) -> Unit = {}
) : RecyclerView.ViewHolder(view) {
    var label: TextView = itemView.findViewById(R.id.label)

    fun bind(suggestion: AutocompleteSuggestion.ProjectSuggestions) {
        itemView.setOnClickListener { onTappedListener(suggestion) }
        label.text = when (suggestion) {
            is AutocompleteSuggestion.ProjectSuggestions.Workspace ->
                suggestion.workspace.name
            is AutocompleteSuggestion.ProjectSuggestions.Client ->
                suggestion.client?.name ?: getString(R.string.no_client)
            is AutocompleteSuggestion.ProjectSuggestions.CreateClient ->
                "${getString(R.string.create_client)} ${suggestion.name}"
        }
    }
}
