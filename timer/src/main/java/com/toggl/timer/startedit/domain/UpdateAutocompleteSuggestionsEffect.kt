package com.toggl.timer.startedit.domain

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.core.Effect
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.startedit.util.lastSubstringFromAnyTokenToPosition
import kotlinx.coroutines.withContext

class UpdateAutocompleteSuggestionsEffect(
    private val dispatcherProvider: DispatcherProvider,
    private val query: String,
    private val cursorPosition: Int,
    private val tags: Map<Long, Tag>,
    private val tasks: Map<Long, Task>,
    private val clients: Map<Long, Client>,
    private val projects: Map<Long, Project>,
    private val timeEntries: Map<Long, TimeEntry>
) : Effect<StartEditAction.AutocompleteSuggestionsUpdated> {

    private val wordSeparator = " "
    private val projectShortCut = '@'
    private val shortcutTokens = charArrayOf(projectShortCut)

    override suspend fun execute(): StartEditAction.AutocompleteSuggestionsUpdated? =
        withContext(dispatcherProvider.computation) {
            if (query.isBlank())
                return@withContext StartEditAction.AutocompleteSuggestionsUpdated(emptyList())

            val (token, actualQuery) = query.lastSubstringFromAnyTokenToPosition(shortcutTokens, cursorPosition)
            val suggestions = when (token) {
                projectShortCut -> fetchProjectSuggestionsFor(actualQuery) + fetchTasksSuggestionsFor(actualQuery)
                else -> fetchTimeEntrySuggestionsFor(actualQuery)
            }
            StartEditAction.AutocompleteSuggestionsUpdated(suggestions)
        }

    private fun fetchTimeEntrySuggestionsFor(query: String): List<AutocompleteSuggestion> {

        fun TimeEntry.projectOrClientNameContains(word: String): Boolean {
            val project = projectId?.run(projects::get) ?: return false
            if (project.name.contains(word, true))
                return true

            val client = project.clientId?.run(clients::get) ?: return false
            return client.name.contains(word, true)
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

        return fetchSuggestionsFor(query, timeEntries.values, AutocompleteSuggestion::TimeEntry) { timeEntry, word ->
            timeEntry.description.contains(word, true) ||
                timeEntry.projectOrClientNameContains(word) ||
                timeEntry.tagNamesContain(word) ||
                timeEntry.taskNameContains(word)
        }
    }

    private fun fetchProjectSuggestionsFor(query: String): List<AutocompleteSuggestion> {
        fun Project.clientNameContains(word: String): Boolean {
            val client = clientId?.run(clients::get) ?: return false
            return client.name.contains(word, true)
        }

        return listOf(AutocompleteSuggestion.CreateProject(query)) +
            fetchSuggestionsFor(query, projects.values, AutocompleteSuggestion::Project) { project, word ->
                project.name.contains(word, true) ||
                    project.clientNameContains(word)
            }
    }

    private fun fetchTasksSuggestionsFor(query: String): List<AutocompleteSuggestion> =
        fetchSuggestionsFor(query, tasks.values, AutocompleteSuggestion::Task) { task, word ->
            task.name.contains(word, true)
        }

    private fun <T> fetchSuggestionsFor(
        query: String,
        possibleSuggestions: Collection<T>,
        toSuggestion: (T) -> AutocompleteSuggestion,
        predicate: (T, String) -> Boolean
    ): List<AutocompleteSuggestion> {
        val words = query.split(wordSeparator)
        return words.fold(possibleSuggestions) { remainingPossibleSuggestions, word ->
            remainingPossibleSuggestions.filter { predicate(it, word) }
        }.map(toSuggestion)
    }
}
