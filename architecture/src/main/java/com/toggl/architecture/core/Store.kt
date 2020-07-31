package com.toggl.architecture.core

import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.StoreScopeProvider
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface Store<State, Action : Any> {
    val state: Flow<State>
    fun dispatch(action: Action)
    fun dispatch(actions: List<Action>)

    fun <ViewState, ViewAction : Any> view(
        mapToLocalState: (State) -> ViewState,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction>

    fun <ViewState : Any, ViewAction : Any> optionalView(
        mapToLocalState: (State) -> ViewState?,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction>
}

class FlowStore<State, Action : Any> private constructor(
    override val state: Flow<State>,
    private val dispatchFn: (List<Action>) -> Unit
) : Store<State, Action> {

    override fun <ViewState, ViewAction : Any> view(
        mapToLocalState: (State) -> ViewState,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction> = FlowStore(
        state = state.map { mapToLocalState(it) }.distinctUntilChanged(),
        dispatchFn = { actions ->
            val globalActions = actions.mapNotNull(mapToGlobalAction)
            dispatchFn(globalActions)
        }
    )

    override fun <ViewState : Any, ViewAction : Any> optionalView(
        mapToLocalState: (State) -> ViewState?,
        mapToGlobalAction: (ViewAction) -> Action?
    ): Store<ViewState, ViewAction> = FlowStore(
        state = state.mapNotNull { mapToLocalState(it) }.distinctUntilChanged(),
        dispatchFn = { actions ->
            val globalActions = actions.mapNotNull(mapToGlobalAction)
            dispatchFn(globalActions)
        }
    )

    companion object {
        fun <State, Action : Any> create(
            initialState: State,
            reducer: Reducer<State, Action>,
            subscription: Subscription<State, Action>,
            dispatcherProvider: DispatcherProvider,
            storeScopeProvider: StoreScopeProvider
        ): Store<State, Action> {
            val storeScope = storeScopeProvider.getStoreScope()
            val stateChannel = ConflatedBroadcastChannel<State>()
            storeScope.launch {
                stateChannel.send(initialState)
            }

            val state = stateChannel
                .asFlow()
                .flowOn(dispatcherProvider.main)

            lateinit var dispatch: (List<Action>) -> Unit
            dispatch = { actions ->
                storeScope.launch {
                    var tempState = stateChannel.value
                    val mutableValue = MutableValue({ tempState }) { tempState = it }

                    val effects = actions.flatMap { reducer.reduce(mutableValue, it) }
                    stateChannel.send(tempState)

                    val effectActions = effects.mapNotNull { it.execute() }
                    if (effectActions.isEmpty()) return@launch
                    dispatch(effectActions)
                }
            }

            subscription.subscribe(state)
                .onEach { action -> dispatch(listOf(action)) }
                .launchIn(storeScope)

            return FlowStore(state, dispatch)
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
