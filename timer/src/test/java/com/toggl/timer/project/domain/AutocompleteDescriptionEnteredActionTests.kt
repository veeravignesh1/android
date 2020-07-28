package com.toggl.timer.project.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.models.domain.Client
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.WorkspaceFeature
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.testReduceNoEffects
import com.toggl.timer.common.testReduceState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
@DisplayName("The AutocompleteDescriptionEntered action")
class AutocompleteDescriptionEnteredActionTests : CoroutineTest() {
    private val reducer = createProjectReducer(dispatcherProvider = dispatcherProvider)
    private val initialState = createInitialState()

    @Nested
    @DisplayName("when not querying anything")
    inner class None {
        @Test
        fun `should update the state clearing the suggestions`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(autocompleteQuery = ProjectAutocompleteQuery.WorkspaceQuery("something")),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.None)
            ) { newState ->
                assertThat(newState.autocompleteQuery).isEqualTo(ProjectAutocompleteQuery.None)
                assertThat(newState.autocompleteSuggestions).isEmpty()
            }
        }

        @Test
        fun `no effects are produced`() = runBlockingTest {
            reducer.testReduceNoEffects(
                initialState,
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.None)
            )
        }
    }

    @Nested
    @DisplayName("when querying workspaces")
    inner class WhenQueryingWorkspaces {
        private val workspaces = (1L..10L).map {
            Workspace(
                it,
                if (it % 2L == 0L) "Even Workspace $it" else "Odd Workspace $it",
                listOf(WorkspaceFeature.Pro)
            )
        }.associateBy { it.id }

        @Test
        fun `should update the suggestions with workspaces with matching names`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(workspaces = workspaces),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.WorkspaceQuery("Even"))
            ) { newState ->
                assertThat(newState.autocompleteQuery).isEqualTo(ProjectAutocompleteQuery.WorkspaceQuery("Even"))
                assertThat(newState.autocompleteSuggestions.size).isEqualTo(5)
                newState.autocompleteSuggestions.all { it is AutocompleteSuggestion.ProjectSuggestions.Workspace }
            }
        }

        @Test
        fun `should update the suggestions with all workspaces when no matching workspace name is found`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(workspaces = workspaces),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.WorkspaceQuery("Cool"))
            ) { newState ->
                assertThat(newState.autocompleteQuery).isEqualTo(ProjectAutocompleteQuery.WorkspaceQuery("Cool"))
                assertThat(newState.autocompleteSuggestions.size).isEqualTo(10)
                newState.autocompleteSuggestions.all { it is AutocompleteSuggestion.ProjectSuggestions.Workspace }
            }
        }

        @Test
        fun `should update the suggestions with all workspaces when the query is empty`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(workspaces = workspaces),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.WorkspaceQuery(""))
            ) { newState ->
                assertThat(newState.autocompleteQuery).isEqualTo(ProjectAutocompleteQuery.WorkspaceQuery(""))
                assertThat(newState.autocompleteSuggestions.size).isEqualTo(10)
                newState.autocompleteSuggestions.all { it is AutocompleteSuggestion.ProjectSuggestions.Workspace }
            }
        }

        @Test
        fun `no effects are produced`() = runBlockingTest {
            reducer.testReduceNoEffects(
                initialState,
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.WorkspaceQuery("whatever"))
            )
        }
    }

    @Nested
    @DisplayName("when querying clients")
    inner class WhenQueryingClients {
        private val workspaces = (1L..2L).map { Workspace(it, it.toString(), listOf(WorkspaceFeature.Pro)) }
        private val clients = (1L..20L).map {
            Client(
                it,
                if (it % 2L == 0L) "Even Client $it" else "Odd Client $it",
                workspaceId = if (it <= 10) 1L else 2L
            )
        }

        @Test
        fun `should update the suggestions with matching clients names from the current workspace only`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery("Even C"))
            ) { state ->
                val clientsSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.Client>()
                        .filter { it.client != null }

                assertThat(clientsSuggestions.size).isEqualTo(5)
                clientsSuggestions.all { it.client?.workspaceId == 1L }
            }
        }

        @Test
        fun `should update the suggestions with all clients from the current workspace when no client name matches`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery("Jorge"))
            ) { state ->
                val clientsSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.Client>()
                        .filter { it.client != null }

                assertThat(clientsSuggestions.size).isEqualTo(10)
                clientsSuggestions.all { it.client?.workspaceId == 1L }
            }
        }

        @Test
        fun `should update the suggestions with all clients from the current workspace when the query is empty`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery("Even Client 2"))
            ) { state ->
                val createClientSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.CreateClient>()
                assertThat(createClientSuggestions).isEmpty()
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["Even", "Odd", "", "Odd Client 1", "Even Client 2"])
        fun `should always suggest the option to select no client`(query: String) = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery(query))
            ) { state ->
                val noClientSuggestion =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.Client>()
                        .filter { it.client == null }
                assertThat(noClientSuggestion.size).isEqualTo(1)
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["E", "Ev", "Eve", "Even", "Even Client"])
        fun `should provide suggestions to create a client when there's no matching client with the same name`(query: String) = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery(query))
            ) { state ->
                val createClientSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.CreateClient>()
                assertThat(createClientSuggestions).hasSize(1)
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "  ", "    ", "\n"])
        fun `should not provide a suggestion to create a client when query is empty or whitespace`(query: String) = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery(query))
            ) { state ->
                val createClientSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.CreateClient>()
                assertThat(createClientSuggestions).isEmpty()
            }
        }

        @Test
        fun `should not suggest to create a client when there's a client with the exact same name on the workspace`() = runBlockingTest {
            reducer.testReduceState(
                initialState.copy(
                    workspaces = workspaces.associateBy { it.id },
                    clients = clients.associateBy { it.id },
                    editableProject = initialState.editableProject.copy(workspaceId = 1L)
                ),
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery(""))
            ) { state ->
                val clientsSuggestions =
                    state.autocompleteSuggestions
                        .filterIsInstance<AutocompleteSuggestion.ProjectSuggestions.Client>()
                        .filter { it.client != null }

                assertThat(clientsSuggestions.size).isEqualTo(10)
                clientsSuggestions.all { it.client?.workspaceId == 1L }
            }
        }

        @Test
        fun `no effects are produced`() = runBlockingTest {
            reducer.testReduceNoEffects(
                initialState,
                ProjectAction.AutocompleteDescriptionEntered(ProjectAutocompleteQuery.ClientQuery("whatever"))
            )
        }
    }
}