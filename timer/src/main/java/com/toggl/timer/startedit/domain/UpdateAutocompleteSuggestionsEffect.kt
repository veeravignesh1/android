package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.common.Constants.AutoCompleteSuggestions.projectToken
import com.toggl.common.Constants.AutoCompleteSuggestions.tagToken
import com.toggl.models.common.AutocompleteSuggestion.StartEditSuggestions
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.startedit.util.findTokenAndQueryMatchesForAutocomplete
import kotlinx.coroutines.withContext

class UpdateAutocompleteSuggestionsEffect(
    private val dispatcherProvider: DispatcherProvider,
    private val query: String,
    private val cursorPosition: Int,
    private val currentWorkspaceId: Long,
    private val tags: Map<Long, Tag>,
    private val tasks: Map<Long, Task>,
    private val clients: Map<Long, Client>,
    private val projects: Map<Long, Project>,
    private val timeEntries: Map<Long, TimeEntry>
) : Effect<StartEditAction.AutocompleteSuggestionsUpdated> {

    private val wordSeparator = " "
    private val shortcutTokens = charArrayOf(tagToken, projectToken)

    override suspend fun execute(): StartEditAction.AutocompleteSuggestionsUpdated? =
        withContext(dispatcherProvider.computation) {
            if (query.isBlank())
                return@withContext StartEditAction.AutocompleteSuggestionsUpdated(emptyList())

            val (token, actualQuery) = query.findTokenAndQueryMatchesForAutocomplete(shortcutTokens, cursorPosition)
            val suggestions = when (token) {
                projectToken -> fetchProjectSuggestionsFor(actualQuery) + fetchTaskSuggestionsFor(actualQuery)
                tagToken -> fetchTagSuggestionsFor(actualQuery)
                else -> fetchTimeEntrySuggestionsFor(actualQuery)
            }
            StartEditAction.AutocompleteSuggestionsUpdated(suggestions)
        }

    private fun fetchTimeEntrySuggestionsFor(query: String): List<StartEditSuggestions> {
        fun TimeEntry.projectNameOrClientContains(word: String): Boolean {
            val project = projectId?.run(projects::get) ?: return false
            return project.nameOrClientContains(word)
        }

        fun TimeEntry.tagNamesContain(word: String): Boolean {
            if (tagIds.isEmpty())
                return false

            return tagIds.mapNotNull(tags::get).any { tag -> tag.name.contains(word, true) }
        }

        fun TimeEntry.taskNameContains(word: String): Boolean {
            val task = taskId?.run(tasks::get) ?: return false
            return task.name.contains(word, true)
        }

        return fetchSuggestionsFor(query, timeEntries.values, StartEditSuggestions::TimeEntry) { timeEntry, word ->
            timeEntry.description.contains(word, true) ||
                timeEntry.projectNameOrClientContains(word) ||
                timeEntry.tagNamesContain(word) ||
                timeEntry.taskNameContains(word)
        }
    }

    private fun fetchProjectSuggestionsFor(query: String): List<StartEditSuggestions> {
        return listOf(StartEditSuggestions.CreateProject(query)) +
            fetchSuggestionsFor(query, projects.values, StartEditSuggestions::Project) { project, word ->
                project.nameOrClientContains(word)
            }
    }

    private fun fetchTaskSuggestionsFor(query: String): List<StartEditSuggestions> =
        fetchSuggestionsFor(query, tasks.values, StartEditSuggestions::Task) { task, word ->
            task.name.contains(word, true)
        }

    private fun fetchTagSuggestionsFor(query: String): List<StartEditSuggestions> {
        val possibleTags = tags.values.filter { it.workspaceId == currentWorkspaceId }

        val tagWithExactNameExistsInWorkspace = possibleTags.any { it.name.equals(query, true) }
        val createSuggestion =
            if (tagWithExactNameExistsInWorkspace || query.isBlank()) emptyList()
            else listOf(StartEditSuggestions.CreateTag(query))

        return createSuggestion + fetchSuggestionsFor(query, possibleTags, StartEditSuggestions::Tag) { tag, word ->
            tag.name.contains(word, true)
        }
    }

    private fun <T> fetchSuggestionsFor(
        query: String,
        possibleSuggestions: Collection<T>,
        toSuggestion: (T) -> StartEditSuggestions,
        predicate: (T, String) -> Boolean
    ): List<StartEditSuggestions> {

        if (possibleSuggestions.isEmpty())
            return emptyList()

        val words = query.split(wordSeparator)
        return words.fold(possibleSuggestions) { remainingPossibleSuggestions, word ->
            remainingPossibleSuggestions.filter { predicate(it, word) }
        }.map(toSuggestion)
    }

    private fun Project.nameOrClientContains(word: String): Boolean {
        if (name.contains(word, true))
            return true

        val client = clientId?.run(clients::get) ?: return false
        return client.name.contains(word, true)
    }
}
