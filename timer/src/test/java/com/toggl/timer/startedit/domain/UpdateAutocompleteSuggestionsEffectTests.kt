package com.toggl.timer.startedit.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.common.AutocompleteSuggestion.StartEditSuggestions
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.project.domain.createProject
import com.toggl.timer.project.domain.createTask

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@DisplayName("The UpdateAutocompleteSuggestionsEffect")
class UpdateAutocompleteSuggestionsEffectTests : CoroutineTest() {

    @Nested
    @DisplayName("When no shortcuts are used")
    inner class ShortcutlessQueryTests {
        @Test
        fun `returns time entries when the description matches the query`() = runBlockingTest {

            val timeEntries = (1L..10L).map { createTimeEntry(it, description = if (it % 2 == 0L) "Even $it" else "Odd $it") }

            val effect = createEffect(
                "even",
                0,
                timeEntries = timeEntries.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>().size).isEqualTo(5)
        }

        @Test
        fun `returns time entries when the project name matches the query`() = runBlockingTest {

            val projects = listOf(createProject(1, name = "project"))
            val timeEntries = (1L..10L).map { createTimeEntry(it, projectId = if (it % 2 == 0L) null else 1L) }

            val effect = createEffect(
                "proj",
                0,
                projects = projects.associateBy { it.id },
                timeEntries = timeEntries.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>().size).isEqualTo(5)
        }

        @Test
        fun `returns time entries when the client name matches the query`() = runBlockingTest {

            val projects = listOf(createProject(1, name = "project", clientId = 1))
            val clients = listOf(Client(1, "client", 1))
            val timeEntries = (1L..10L).map { createTimeEntry(it, projectId = if (it % 2 == 0L) null else 1L) }

            val effect = createEffect(
                "clien",
                0,
                clients = clients.associateBy { it.id },
                projects = projects.associateBy { it.id },
                timeEntries = timeEntries.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>().size).isEqualTo(5)
        }

        @Test
        fun `returns time entries when the task name matches the query`() = runBlockingTest {

            val tasks = listOf(Task(1, "task", true, 1, 1, 1))
            val timeEntries = (1L..10L).map { createTimeEntry(it, taskId = if (it % 2 == 0L) null else 1L) }

            val effect = createEffect(
                "tas",
                0,
                tasks = tasks.associateBy { it.id },
                timeEntries = timeEntries.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>().size).isEqualTo(5)
        }

        @Test
        fun `matches multiple words for time entry descriptions`() = runBlockingTest {

            val timeEntries = listOf(
                createTimeEntry(1, description = "Android App Development"), // Matches
                createTimeEntry(2, description = "Another apt technology to be developed"), // Matches
                createTimeEntry(3, description = "Anthropomorphic apartment devs"), // Matches
                createTimeEntry(4, description = "ANTS APPEAR MORE DEVELOPED HERE"), // Matches
                createTimeEntry(5, description = "iOS App development"),
                createTimeEntry(6, description = "Another technology"),
                createTimeEntry(7, description = "Meetings")
            )

            val effect = createEffect(
                "an ap dev",
                0,
                timeEntries = timeEntries.associateBy { it.id }
            )

            val expectedEntries = timeEntries.take(4)
            val result = effect.execute()
            assertThat(result).isNotNull()

            val timeEntrySuggestions = result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>()
            assertThat(timeEntrySuggestions.size).isEqualTo(expectedEntries.size)
            assertThat(timeEntrySuggestions.all { expectedEntries.contains(it.timeEntry) }).isTrue()
        }

        @Test
        fun `matches multiple words for when the matches happen in different entities`() = runBlockingTest {

            val tasks = listOf(
                Task(1, "ants", true, 1, 1, 1)
            )

            val clients = listOf(
                Client(1, "devs", 1)
            )

            val projects = listOf(
                createProject(1, name = "App"),
                createProject(2, name = "dunno", clientId = 1)
            )
            val tags = listOf(
                Tag(1, "ants", 1),
                Tag(2, "app", 1)
            )

            val timeEntries = listOf(
                createTimeEntry(1, description = "Android App Development"), // Matches by description
                createTimeEntry(
                    2,
                    description = "Another technology to be developed",
                    projectId = 1
                ), // Matches by description and project
                createTimeEntry(
                    3,
                    description = "Anthropomorphic apartment",
                    projectId = 2
                ), // Matches by description and client name
                createTimeEntry(4, description = "", projectId = 2, tags = listOf(1, 2)), // Matches by client and tags
                createTimeEntry(5, description = "iOS App development", taskId = 1), // Matches by description and task
                createTimeEntry(6, description = "Another technology"),
                createTimeEntry(7, description = "Meetings")
            )

            val effect = createEffect(
                "an ap dev",
                0,
                tags = tags.associateBy { it.id },
                tasks = tasks.associateBy { it.id },
                clients = clients.associateBy { it.id },
                projects = projects.associateBy { it.id },
                timeEntries = timeEntries.associateBy { it.id }
            )

            val expectedEntries = timeEntries.take(5)
            val result = effect.execute()
            assertThat(result).isNotNull()

            val timeEntrySuggestions = result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.TimeEntry>()
            assertThat(timeEntrySuggestions.size).isEqualTo(expectedEntries.size)
            assertThat(timeEntrySuggestions.all { expectedEntries.contains(it.timeEntry) }).isTrue()
        }
    }

    @Nested
    @DisplayName("When a project query shortcut is used")
    inner class ProjectQueryTests {
        @Test
        fun `returns projects & a create project suggestion when the project name matches the query`() = runBlockingTest {
            val projects = (1L..10L).map { createProject(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it") }

            val effect = createEffect(
                "@even",
                5,
                projects = projects.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Project>().size).isEqualTo(5)
            assertThat(result.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateProject>().size).isEqualTo(1)
        }

        @Test
        fun `returns tasks when the task name matches the query`() = runBlockingTest {
            val tasks = (1L..10L).map { createTask(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it") }

            val effect = createEffect(
                "@even",
                5,
                tasks = tasks.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Task>().size).isEqualTo(5)
            assertThat(result.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateProject>().size).isEqualTo(1)
        }

        @Test
        fun `returns a create project suggestion with the query as the suggestion name`() = runBlockingTest {
            val projects = (1L..10L).map { createProject(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it") }

            val effect = createEffect(
                "@even",
                5,
                projects = projects.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateProject>().first().name).isEqualTo("even")
        }

        @Test
        fun `matches multiple words for project names`() = runBlockingTest {
            val projects = listOf(
                createProject(1, name = "Android App Development"), // Matches
                createProject(2, name = "Another apt technology to be developed"), // Matches
                createProject(3, name = "Anthropomorphic apartment devs"), // Matches
                createProject(4, name = "ANTS APPEAR MORE DEVELOPED HERE"), // Matches
                createProject(5, name = "iOS App development"),
                createProject(6, name = "Another technology"),
                createProject(7, name = "Meetings")
            )

            val effect = createEffect(
                "@an ap dev",
                10,
                projects = projects.associateBy { it.id }
            )

            val expectedEntries = projects.take(4)
            val result = effect.execute()
            assertThat(result).isNotNull()

            val projectSuggestions = result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Project>()
            assertThat(projectSuggestions.size).isEqualTo(expectedEntries.size)
            assertThat(projectSuggestions.all { expectedEntries.contains(it.project) }).isTrue()
        }

        @Test
        fun `returns the projects which the client names matches the query`() = runBlockingTest {
            val clients = listOf(
                Client(1, "bob", 1),
                Client(2, "fred", 1)
            )
            val projects = (1L..10L).map { createProject(it, name = "Project $it", clientId = it % 2 + 1) }

            val effect = createEffect(
                "@bo",
                4,
                clients = clients.associateBy { it.id },
                projects = projects.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Project>().size).isEqualTo(5)
        }
    }

    @Nested
    @DisplayName("When a tag query shortcut is used")
    inner class TagQueryTests {
        @Test
        fun `returns tag suggestions when the tag name matches the query`() = runBlockingTest {
            val tags = (1L..10L).map { Tag(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it", workspaceId = 1) }

            val effect = createEffect(
                "#even",
                5,
                tags = tags.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Tag>().size).isEqualTo(5)
        }

        @Test
        fun `returns a create tag suggestion when the tag name matches the query but not exactly`() = runBlockingTest {
            val tags = (1L..10L).map { Tag(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it", workspaceId = 1) }

            val effect = createEffect(
                "#eve",
                5,
                tags = tags.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateTag>().single().name).isEqualTo("eve")
        }

        @Test
        fun `does not return a create tag suggestion when the tag name matches the query exactly`() = runBlockingTest {
            val tags = listOf(Tag(1, name = "Even", workspaceId = 1))

            val effect = createEffect(
                "#even",
                5,
                tags = tags.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateTag>()).isEmpty()
        }

        @Test
        fun `only considers tags in the current workspace`() = runBlockingTest {
            val tags = (1L..10L).map { Tag(it, name = if (it % 2 == 0L) "Even $it" else "Odd $it", workspaceId = 2) }

            val effect = createEffect(
                "#even",
                5,
                tags = tags.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.Tag>()).isEmpty()
        }

        @Test
        fun `returns a create tag suggestion if the query matches exactly a tag in a different workspace`() = runBlockingTest {
            val tags = listOf(Tag(1, name = "Even", workspaceId = 2))

            val effect = createEffect(
                "#even",
                5,
                tags = tags.associateBy { it.id }
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions.filterIsInstance<StartEditSuggestions.CreateTag>().single().name).isEqualTo("even")
        }

        @Test
        fun `shouldn't return a create tag suggestion if the query is blank`() = runBlockingTest {
            val effect = createEffect(
                "#  ",
                5,
                tags = emptyMap()
            )

            val result = effect.execute()
            assertThat(result).isNotNull()
            assertThat(result!!.autocompleteSuggestions).isEmpty()
        }
    }

    private fun createEffect(
        query: String,
        cursorPosition: Int,
        currentWorkspaceId: Long = 1,
        tags: Map<Long, Tag> = mapOf(),
        tasks: Map<Long, Task> = mapOf(),
        clients: Map<Long, Client> = mapOf(),
        projects: Map<Long, Project> = mapOf(),
        timeEntries: Map<Long, TimeEntry> = mapOf()
    ) = UpdateAutocompleteSuggestionsEffect(
        dispatcherProvider,
        query,
        cursorPosition,
        currentWorkspaceId,
        tags,
        tasks,
        clients,
        projects,
        timeEntries)
}