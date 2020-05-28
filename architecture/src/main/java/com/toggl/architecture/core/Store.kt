package com.toggl.architecture.core

import com.toggl.architecture.StoreScopeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface Store<State, Action : Any> {
    val state: Flow<State>
    fun dispatch(action: Action)
    fun dispatch(actions: List<Action>)

    @ExperimentalCoroutinesApi
    fun <ViewState, ViewAction : Any> view(
        mapToLocalState: (State) -> ViewState,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction>
}

class FlowStore<State, Action : Any> private constructor(
    override val state: Flow<State>,
    private val dispatchFn: (List<Action>) -> Unit
) : Store<State, Action> {
    @ExperimentalCoroutinesApi
    override fun <ViewState, ViewAction : Any> view(
        mapToLocalState: (State) -> ViewState,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction> {
        return FlowStore(
            state = state.map { mapToLocalState(it) }.distinctUntilChanged(),
            dispatchFn = { actions ->
                val globalActions = actions.mapNotNull(mapToGlobalAction)
                dispatchFn(globalActions)
            }
        )
    }

    companion object {
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun <State, Action : Any> create(
            initialState: State,
            reducer: Reducer<State, Action>,
            storeScopeProvider: StoreScopeProvider
        ): Store<State, Action> {
            val storeScope = storeScopeProvider.getStoreScope()
            val stateFlow = MutableStateFlow(initialState)

            lateinit var dispatch: (List<Action>) -> Unit
            dispatch = { actions ->
                storeScope.launch {
                    var tempState = stateFlow.value
                    val mutableValue = MutableValue({ tempState }) { tempState = it }

                    val effects = actions.flatMap { reducer.reduce(mutableValue, it) }
                    stateFlow.value = tempState

                    val effectActions = effects.mapNotNull { it.execute() }
                    if (effectActions.isEmpty()) return@launch
                    dispatch(effectActions)
                }
            }

            return FlowStore(stateFlow, dispatch)
        }
    }

    override fun dispatch(action: Action) =
        dispatchFn(listOf(action))

    override fun dispatch(actions: List<Action>) {
        if (actions.isEmpty())
            return

        dispatchFn(actions)
    }
}
