package com.toggl.timer.suggestions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toggl.common.feature.domain.formatForDisplay
import com.toggl.timer.R
import com.toggl.timer.suggestions.domain.Suggestion
import com.toggl.timer.suggestions.domain.SuggestionViewModel

class SuggestionsAdapter(
    private val onContinueTappedListener: (Suggestion) -> Unit
) : ListAdapter<SuggestionViewModel, SuggestionsViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionsViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.time_entries_log_suggestion, parent, false)
            .let(::SuggestionsViewHolder)

    override fun onBindViewHolder(holder: SuggestionsViewHolder, position: Int) {
        holder.bind(getItem(position), onContinueTappedListener)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SuggestionViewModel>() {
            override fun areItemsTheSame(
                oldItem: SuggestionViewModel,
                newItem: SuggestionViewModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SuggestionViewModel,
                newItem: SuggestionViewModel
            ): Boolean = oldItem == newItem
        }
    }
}

class SuggestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val description: TextView = itemView.findViewById(R.id.description)
    private val project: TextView = itemView.findViewById(R.id.project_label)

    fun bind(item: SuggestionViewModel, onContinueTappedListener: (Suggestion) -> Unit) {
        val hasDescription = item.description.isNotBlank()
        description.isVisible = hasDescription
        description.text = item.description

        val formattedProjectText = item.project.formatForDisplay()
        project.text = formattedProjectText
        project.isVisible = formattedProjectText.isNotBlank()

        itemView.setOnClickListener {
            onContinueTappedListener(item.suggestion)
        }
    }
}
