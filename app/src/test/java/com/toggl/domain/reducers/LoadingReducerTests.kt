package com.toggl.domain.reducers

import com.toggl.common.FreeCoroutineSpec
import com.toggl.domain.extensions.createClient
import com.toggl.domain.extensions.createProject
import com.toggl.domain.extensions.createTag
import com.toggl.domain.extensions.createTask
import com.toggl.domain.extensions.createTimeEntry
import com.toggl.domain.extensions.toMutableValue
import com.toggl.domain.loading.LoadClientsEffect
import com.toggl.domain.loading.LoadTagsEffect
import com.toggl.domain.loading.LoadTasksEffect
import com.toggl.domain.loading.LoadUserPreferencesEffect
import com.toggl.domain.loading.LoadWorkspacesEffect
import com.toggl.domain.loading.LoadingAction
import com.toggl.domain.loading.LoadingReducer
import com.toggl.domain.loading.LoadingState
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SmartAlertsOption
import com.toggl.models.domain.UserPreferences
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.WorkspaceFeature
import com.toggl.repository.interfaces.ClientRepository
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.repository.interfaces.TagRepository
import com.toggl.repository.interfaces.TaskRepository
import com.toggl.repository.interfaces.WorkspaceRepository
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.DayOfWeek

@ExperimentalCoroutinesApi
class LoadingReducerTests : FreeCoroutineSpec() {
    init {
        val projectRepository = mockk<ProjectRepository>()
        val clientRepository = mockk<ClientRepository>()
        val workspaceRepository = mockk<WorkspaceRepository>()
        val tagRepository = mockk<TagRepository>()
        val taskRepository = mockk<TaskRepository>()
        val settingsRepository = mockk<SettingsRepository>()
        val reducer = LoadingReducer(
            projectRepository,
            clientRepository,
            workspaceRepository,
            tagRepository,
            taskRepository,
            settingsRepository,
            dispatcherProvider
        )
        val emptyState = LoadingState(listOf(), listOf(), listOf(), listOf(), listOf(), listOf(),
            UserPreferences(
                manualModeEnabled = true,
                twentyFourHourClockEnabled = false,
                selectedWorkspaceId = 1,
                dateFormat = DateFormat.DDMMYYYY_dash,
                durationFormat = DurationFormat.Decimal,
                firstDayOfTheWeek = DayOfWeek.MONDAY,
                groupSimilarTimeEntriesEnabled = true,
                cellSwipeActionsEnabled = true,
                smartAlertsOption = SmartAlertsOption.Disabled,
                calendarIntegrationEnabled = true,
                calendarIds = emptyList()
            )
        )

        "The LoadingReducer" - {
            "when receiving a Start Loading action" - {

                "does not update the state" {
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.StartLoading)

                    initialState shouldBe emptyState
                }

                "returns a list of effects that load entities" {
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    val effects = reducer.reduce(mutableValue, LoadingAction.StartLoading)

                    effects.map { it.javaClass.kotlin } shouldContainExactlyInAnyOrder listOf(
                        LoadWorkspacesEffect::class,
                        LoadClientsEffect::class,
                        LoadTagsEffect::class,
                        LoadTasksEffect::class,
                        LoadUserPreferencesEffect::class
                    )
                }
            }

            "when receiving a Time Entries Loaded action" - {

                "updates the state to add the loaded time entries" - {
                    val entries = listOf(createTimeEntry(1), createTimeEntry(2), createTimeEntry(3))
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.TimeEntriesLoaded(entries))

                    initialState shouldBe emptyState.copy(timeEntries = entries)
                }
            }

            "when receiving a Workspaces Loaded action" - {

                "updates the state to add the loaded workspaces" - {
                    val workspaces = listOf(
                        Workspace(1, "1", listOf()),
                        Workspace(2, "2", listOf(WorkspaceFeature.Pro)),
                        Workspace(3, "3", listOf())
                    )
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.WorkspacesLoaded(workspaces))

                    initialState shouldBe emptyState.copy(workspaces = workspaces)
                }
            }

            "when receiving a Projects Loaded action" - {

                "updates the state to add the loaded projects" - {
                    val projects = (1L..10L).map { createProject(it) }
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.ProjectsLoaded(projects))

                    initialState shouldBe emptyState.copy(projects = projects)
                }
            }

            "when receiving a Clients Loaded action" - {

                "updates the state to add the loaded clients" - {
                    val clients = (1L..10L).map { createClient(it) }
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.ClientsLoaded(clients))

                    initialState shouldBe emptyState.copy(clients = clients)
                }
            }

            "when receiving a Tags Loaded action" - {

                "updates the state to add the loaded tags" - {
                    val tags = (1L..10L).map { createTag(it) }
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.TagsLoaded(tags))

                    initialState shouldBe emptyState.copy(tags = tags)
                }
            }

            "when receiving a Tasks Loaded action" - {

                "updates the state to add the loaded tasks" - {
                    val tasks = (1L..10L).map { createTask(it) }
                    var initialState = emptyState
                    val mutableValue = initialState.toMutableValue { initialState = it }
                    reducer.reduce(mutableValue, LoadingAction.TasksLoaded(tasks))

                    initialState shouldBe emptyState.copy(tasks = tasks)
                }
            }
        }
    }
}