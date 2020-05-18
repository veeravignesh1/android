package com.toggl.timer.startedit.domain

import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Project
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.createTask
import com.toggl.timer.common.createTimeEntry
import com.toggl.timer.common.domain.EditableProject
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.common.testReduceEffects
import com.toggl.timer.common.testReduceException
import com.toggl.timer.common.testReduceNoEffects
import com.toggl.timer.common.testReduceState
import com.toggl.timer.project.domain.createProject
import com.toggl.timer.exceptions.ProjectDoesNotExistException
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExperimentalCoroutinesApi
@DisplayName("The AutocompleteSuggestionTapped action")
internal class AutocompleteSuggestionTappedActionTests : CoroutineTest() {
    val initialState = createInitialState()
    val reducer = createReducer()

    @Nested
    @DisplayName("When a TimeEntry suggestion is tapped")
    inner class TimeEntrySuggestions : TheoryHolder {

        @ParameterizedTest
        @MethodSource("timeEntries")
        fun `The editableTimeEntry details should be updated`(timeEntrySuggestion: TimeEntry) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(1)
            val suggestion = AutocompleteSuggestion.TimeEntry(timeEntrySuggestion)

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        description = timeEntrySuggestion.description,
                        billable = timeEntrySuggestion.billable,
                        projectId = timeEntrySuggestion.projectId,
                        tagIds = timeEntrySuggestion.tagIds,
                        workspaceId = timeEntrySuggestion.workspaceId,
                        taskId = timeEntrySuggestion.taskId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("timeEntries")
        fun `should return no effect`(timeEntrySuggestion: TimeEntry) = runBlockingTest {
            val suggestion = AutocompleteSuggestion.TimeEntry(timeEntrySuggestion)

            reducer.testReduceNoEffects(
                initialState,
                action = StartEditAction.AutocompleteSuggestionTapped(suggestion)
            )
        }
    }

    @Nested
    @DisplayName("When a Project suggestion is tapped")
    inner class ProjectSuggestions : TheoryHolder {

        @ParameterizedTest
        @MethodSource("projects")
        fun `should set the project information to the editable time entry`(project: Project) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(1)
            val suggestion = AutocompleteSuggestion.Project(project)

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        projectId = project.id,
                        workspaceId = project.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("projects")
        fun `should clear any editable project information in the editable time entry`(project: Project) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(1).copy(
                editableProject = EditableProject(workspaceId = 1)
            )
            val suggestion = AutocompleteSuggestion.Project(project)

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        editableProject = null,
                        projectId = project.id,
                        workspaceId = project.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("projects")
        fun `should remove the substring that starts with an '@' from the description`(project: Project) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such @${project.name}"
            )
            val suggestion = AutocompleteSuggestion.Project(project)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        description = "Such ",
                        projectId = project.id,
                        workspaceId = project.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("projects")
        fun `should remove the last substring that starts with an '@' from the description when multiple tokens are in place`(
            project: Project
        ) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such # @ @ @ @${project.name}"
            )
            val suggestion = AutocompleteSuggestion.Project(project)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        description = "Such # @ @ @ ",
                        projectId = project.id,
                        workspaceId = project.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("projects")
        fun `should remove the last substring that starts with an '@' from the description when multiple tokens are in place up to the cursor position`(
            project: Project
        ) = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such # @ # @ #@${project.name} Description"
            )
            val suggestion = AutocompleteSuggestion.Project(project)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length - " Description".length
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        description = "Such # @ # @ # Description",
                        projectId = project.id,
                        workspaceId = project.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("projects")
        fun `should return no effect`(project: Project) = runBlockingTest {
            val suggestion = AutocompleteSuggestion.Project(project)

            reducer.testReduceNoEffects(
                initialState,
                action = StartEditAction.AutocompleteSuggestionTapped(suggestion)
            )
        }
    }

    @Nested
    @DisplayName("When the Create Project suggestion is tapped")
    inner class CreateProjectSuggestions {

        private val projectName = "Project name"
        private val suggestion = AutocompleteSuggestion.CreateProject(projectName)

        @Test
        fun `The editableProject should be initialized`() = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(1)

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it shouldBe initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        editableProject = EditableProject(name = projectName, workspaceId = 1)
                    )
                )
            }
        }

        @Test
        fun `should return no effect`() = runBlockingTest {
            reducer.testReduceNoEffects(
                initialState,
                action = StartEditAction.AutocompleteSuggestionTapped(suggestion)
            )
        }
    }

    @Nested
    @DisplayName("When a Task suggestion is tapped")
    inner class TaskSuggestions : TheoryHolder {

        @ParameterizedTest
        @MethodSource("tasks")
        fun `editableTimeEntry details should be updated`(taskTestData: TheoryHolder.TaskTestData) = runBlockingTest {
            val testTask = taskTestData.task
            val testProjects = taskTestData.projects

            val initialEditableTimeEntry = EditableTimeEntry.empty(1)
            val initialStateWithProjects = initialState.copy(projects = testProjects)
            val suggestion = AutocompleteSuggestion.Task(testTask)

            reducer.testReduceState(
                initialStateWithProjects,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                // let's leave out the description for now, it will be testes separately
                val descriptionLessState = it.copy(editableTimeEntry = it.editableTimeEntry!!.copy(description = ""))
                descriptionLessState shouldBe initialStateWithProjects.copy(
                    editableTimeEntry = initialEditableTimeEntry.copy(
                        projectId = testTask.projectId,
                        description = "",
                        taskId = testTask.id,
                        workspaceId = testTask.workspaceId
                    )
                )
            }
        }

        @ParameterizedTest
        @MethodSource("tasks")
        fun `should return no effect`(taskTestData: TheoryHolder.TaskTestData) = runBlockingTest {
            val suggestion = AutocompleteSuggestion.Task(taskTestData.task)
            val initialStateWithProjects = initialState.copy(projects = taskTestData.projects)

            reducer.testReduceNoEffects(
                initialStateWithProjects,
                action = StartEditAction.AutocompleteSuggestionTapped(suggestion)
            )
        }

        @ParameterizedTest
        @MethodSource("tasksWithNonExistingProjects")
        fun `should throw exception when task points to a non existent project`(taskTestData: TheoryHolder.TaskWithNonExistingProjectTestData) = runBlockingTest {
            val suggestion = AutocompleteSuggestion.Task(taskTestData.task)
            val initialStateWithProjects = initialState.copy(projects = taskTestData.projects)

            reducer.testReduceException(
                initialStateWithProjects,
                action = StartEditAction.AutocompleteSuggestionTapped(suggestion),
                exception = ProjectDoesNotExistException::class.java
            )
        }

        @ParameterizedTest
        @MethodSource("tasks")
        fun `should remove the substring that starts with an '@' from the description`(taskTestData: TheoryHolder.TaskTestData) = runBlockingTest {
            val task = taskTestData.task
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such @${task.name}"
            )
            val suggestion = AutocompleteSuggestion.Task(task)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length,
                projects = taskTestData.projects
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it.editableTimeEntry?.description shouldBe "Such "
            }
        }

        @ParameterizedTest
        @MethodSource("tasks")
        fun `should remove the last substring that starts with an '@' from the description when multiple tokens are in place`(
            taskTestData: TheoryHolder.TaskTestData
        ) = runBlockingTest {
            val task = taskTestData.task
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such # @ @ @ @${task.name}"
            )
            val suggestion = AutocompleteSuggestion.Task(task)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length,
                projects = taskTestData.projects
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it.editableTimeEntry?.description shouldBe "Such # @ @ @ "
            }
        }

        @ParameterizedTest
        @MethodSource("tasks")
        fun `should remove the last substring that starts with an '@' from the description when multiple tokens are in place up to the cursor position`(
            taskTestData: TheoryHolder.TaskTestData
        ) = runBlockingTest {
            val task = taskTestData.task
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such # @ # @ #@${task.name} Description"
            )
            val suggestion = AutocompleteSuggestion.Task(task)
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length - " Description".length,
                projects = taskTestData.projects
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it.editableTimeEntry?.description shouldBe "Such # @ # @ # Description"
            }
        }
    }

    @Nested
    @DisplayName("When the Create Tag suggestion is tapped")
    inner class CreateTagSuggestions {

        @Test
        fun `The TagCreated effect is emitted`() = runBlockingTest {
            val createTagSuggestion = AutocompleteSuggestion.CreateTag("01234")

            reducer.testReduceEffects(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(createTagSuggestion)
            ) {
                it shouldHaveSize 1
                it.single()
                    .shouldBeTypeOf<CreateTagEffect>()
            }
        }

        @Test
        fun `The substring that starts with an '#' should be removed from the description`() = runBlockingTest {
            val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                description = "Such #tag"
            )
            val suggestion = AutocompleteSuggestion.CreateTag("tag")
            val initialState = initialState.copy(
                editableTimeEntry = initialEditableTimeEntry,
                cursorPosition = initialEditableTimeEntry.description.length
            )

            reducer.testReduceState(
                initialState,
                StartEditAction.AutocompleteSuggestionTapped(suggestion)
            ) {
                it.editableTimeEntry?.description shouldBe "Such "
            }
        }

        @Test
        fun `should remove the last substring that starts with an '#' from the description when multiple tokens are in place up to the cursor position`() =
            runBlockingTest {
                val initialEditableTimeEntry = EditableTimeEntry.empty(10).copy(
                    description = "Such #tag #tag #tag much"
                )
                val suggestion = AutocompleteSuggestion.CreateTag("tag much")
                val initialState = initialState.copy(
                    editableTimeEntry = initialEditableTimeEntry,
                    cursorPosition = initialEditableTimeEntry.description.length
                )

                reducer.testReduceState(
                    initialState,
                    StartEditAction.AutocompleteSuggestionTapped(suggestion)
                ) {
                    it.editableTimeEntry?.description shouldBe "Such #tag #tag "
                }
            }
    }

    interface TheoryHolder {
        companion object {

            @JvmStatic
            fun timeEntries(): Stream<TimeEntry> = Stream.of(
                createTimeEntry(10),
                createTimeEntry(20, "Expected Description"),
                createTimeEntry(30, billable = true),
                createTimeEntry(40, billable = false),
                createTimeEntry(50, projectId = 10),
                createTimeEntry(60, workspaceId = 10),
                createTimeEntry(80, taskId = 10),
                createTimeEntry(70, tags = listOf(10, 20)),
                createTimeEntry(
                    80,
                    "Expected Description",
                    billable = true,
                    projectId = 20,
                    workspaceId = 20,
                    tags = listOf(30),
                    taskId = 20
                )
            )

            @JvmStatic
            fun projects(): Stream<Project> = Stream.of(
                createProject(1),
                createProject(10, workspaceId = 10)
            )

            @JvmStatic
            fun tasks(): Stream<TaskTestData> {
                val project1 = Project(1, "a", "", true, false, true, 2, null)
                val project2 = Project(2, "b", "", true, false, true, 3, null)
                return Stream.of(
                    TaskTestData(
                        task = createTask(1, "Test", projectId = 1),
                        projects = mapOf(
                            1L to project1,
                            2L to project2
                        )
                    ),
                    TaskTestData(
                        task = createTask(2, "Test 2", projectId = 2),
                        projects = mapOf(
                            1L to project1,
                            2L to project2
                        )
                    ),
                    TaskTestData(
                        task = createTask(2, "Test 2", true, projectId = 2),
                        projects = mapOf(
                            1L to project1,
                            2L to project2
                        )
                    ),
                    TaskTestData(
                        task = createTask(2, "Test 2", true, projectId = 2),
                        projects = mapOf(
                            1L to project1,
                            2L to project2
                        )
                    )
                )
            }

            @JvmStatic
            fun tasksWithNonExistingProjects(): Stream<TaskWithNonExistingProjectTestData> = Stream.of(
                TaskWithNonExistingProjectTestData(
                    task = createTask(1, "Test", projectId = 1),
                    projects = mapOf()
                ),
                TaskWithNonExistingProjectTestData(
                    task = createTask(1, "Test", projectId = 3),
                    projects = mapOf(
                        1L to Project(1, "a", "", true, false, true, 2, null)
                    )
                )
            )
        }

        data class TaskTestData(val task: Task, val projects: Map<Long, Project>)
        data class TaskWithNonExistingProjectTestData(val task: Task, val projects: Map<Long, Project>)
    }
}