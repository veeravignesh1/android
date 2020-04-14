# Architecture documentation

## Sources

The app architecture is based on different sources, it has some of Redux and some of Elm. It's based on the work of [these guys](https://www.pointfree.co) and also a little bit from [this](https://guide.elm-lang.org/architecture/).

One of the principles of this architecture is sharing logic (not code) between both mobile plataforms. This allows Android and iOS developers to discuss both apps using a unified language, which is extremely valuable for defining specs and for reusing solutions from the other platform.

## Parts

This is a high level overview of the different parts of the architecture. 

![architecture](images/architecture.png)

- **Views** This is anything that can subscribe to the store to be notified of state changes. Normally this happens only in UI elements, but other elements of the app could also react to state changes.
- **Action** Simple structs that describe an event, normally originated by the user, but also from other sources or in response to other actions (from Effects). The only way to change the state is through actions. Views dispatch actions to the store which handles them in the main thread as they come.
- **Store** The central hub of the application. Contains the whole state of the app, handles the dispatched actions passing them to the reducers and fires Effects.
- **App State** The single source of truth for the whole app. This will be almost empty when the application start and will be filled after every action. This won't contain any derived state (meaning any state that can be calculated from any other state). This won't be a copy of the DB, it doesn't have to necessarily contain everything in the DB all the time and also, it'll include other stuff not in the DB (like the current route or a flag indicating if the app is in the background, for example). Basically you have to ask yourself what do you need to store here so you can restart the app in the exact same place as it was before shutting it down (even if we are never going to need that)
- **Reducers** Reducers are pure functions that take the state and an action and produce a new state. Simple as that. They optionally result in an array of Effects that will asynchronously dispatch further actions. All business logic should reside in them.
- **Effects** As mentioned, Reducers optionally produce these after handling an action. They are classes that return an optional effect. All the effects emitted from a reducer will be batched, meaning the state change will only be emitted once all actions are dispatched.

There's one global `Store` and one `AppState`. But we can *view* into the store to get sub-stores that only work on one part of the state. More on that later.

There's also one main `Reducer` but multiple sub-reducers that handle a limited set of actions and only a part of the state. Those reducers are then *pulled back* and *combined* into the main 
reducer.

## Differences between platforms

Since the architecture as a whole is agnostic to frameworks, we don't necessarily need to use the same set of frameworks for both apps. While the iOS app is using Rxkotlin for their implementation, we are using Kotlin coroutines and [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) in the Android app. Whenever one uses an `Observable<T>` on iOS, on Android it'd be an `Flow<T>`. Similarly, `Single<T>` maps to `suspend fun foo() : T?`.

Effects are classes that implement the `Effect` interface, while on iOS they are wrappers around `Single<T>`. We did so to ensure the contract of having `suspend fun`s for effects.

Reducers are classes that implement the `Reducer<State, Action>` interface, while on iOS they are all a single `Reducer` type. We chose to do so on Android to massively simplify Dagger injection.

## Store & State

The `Store` exposes a flow which emits the whole state of the app every time there's a change and a method to dispatch actions that will modify that state.  The `State` is just a struct that contains ALL the state of the application. It also includes the local state of all the specific modules that need local state. More on this later.

The store is built like this:

```kotlin
FlowStore.create(
    initialState = AppState(),
    reducer = reducer,
    dispatcherProvider = dispatcherProvider,
    storeScopeProvider = storeScopeProvider
)
```

actions are dispatched like this:

```kotlin
store.dispatch(AppAction.start)
```

and views can subscribe like this:

```kotlin
store.state
    .onEach { Log.d(tag, "The whole state: \($0)") }
    .launchIn(scope)

// or

store.state
    .map { it.email }
    .onEach { emailTextField.text = it }
    .launchIn(scope)
```

The store can be "viewed into", which means that we'll treat a generic store as if it was a more specific one which deals with only part of the app state and a subset of the actions. More on the Store Views section.

## Actions

Actions are sealed classes, which makes it easier to discover which actions are available and also add the certainty that we are handling all of them in reducers.

These sealed actions are embedded into each other starting with the `AppAction`

```kotlin
sealed class AppAction {
    object Start : AppAction()

    data class Onboarding(val onboarding: OnboardingAction) : AppAction()
    data class Timer(val timer: TimerAction) : AppAction()
}
```

So to dispatch an `OnboardingAction` to a store that takes `AppActions` we would do

```kotlin
store.dispatch(AppAction.Onboading(OnboardingAction.Start))
```

But if the store is a view that takes `OnboardingAction`s we'd do it like this:

```kotlin
store.dispatch(OnboardingAction.Start)
```

## Batching actions

Sometimes we might want to dispatch multiple actions one after another. It might not be efficient to emit state changes after each of of those actions, also it might cause problems with some animations, that's why there's the dispatch method on `Store` that takes a list of actions and executes all of them before emitting the new state. It's worth noting that the state itself will update, so every action will get the updated state, but it won't be emitted outside of the store until all of them are done.

## Reducers & Effects

Reducers are classes that implement the following interface:

```kotlin
interface Reducer<State, Action> {
    fun reduce(state: SettableValue<State>, action: Action): List<Effect<Action>>
}
```

The idea is they take the state and an action and modify the state depending on the action and its payload.

In order to dispatch actions asynchronously we use `Effect`s. Reducers return an array of `Effect`s. The store waits for those effects and dispatches whatever action they emit, if any.

## Pullback

There's one app level reducer that gets injected into the store. This reducer takes the whole `AppState` and the complete set of `AppActions`. 

The rest of the reducers only handle one part of that state, for a particular subset of the actions.

This aids in modularity. But in order to merge those reducers with the app level one, their types need to be compatible. That's what `pullback` is for. It converts a specific reducer into a global one.

```kotlin
class PullbackReducer<LocalState, GlobalState, LocalAction, GlobalAction>(
    private val innerReducer: Reducer<LocalState, LocalAction>,
    private val mapToLocalState: (GlobalState) -> LocalState,
    private val mapToLocalAction: (GlobalAction) -> LocalAction?,
    private val mapToGlobalState: (GlobalState, LocalState) -> GlobalState,
    private val mapToGlobalAction: (LocalAction) -> GlobalAction
) : Reducer<GlobalState, GlobalAction> {
    override fun reduce(
        state: SettableValue<GlobalState>,
        action: GlobalAction
    ): List<Effect<GlobalAction>> {
        val localAction = mapToLocalAction(action)
            ?: return noEffect()

        return innerReducer
            .reduce(state.map(mapToLocalState, mapToGlobalState), localAction)
            .map { effect -> effect.map { action -> action?.run(mapToGlobalAction) } }
    }
}
```

After we've transformed the reducer we can use `combine` to merge it with other reducers to create one single reducer that is then injected into the store.

## Store Views

Similarly to reducers and pullback, the store itself can be "mapped" into a specific type of store that only holds some part of the state and only handles some subset of actions. Only this operation is not exactly "map", so it's called `view`.

```kotlin
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
}
```

This method on `Store` takes two closures, one to map the global state into local state and another one to the opposite for the actions.

Different modules or features of the app use different store views so they are only able to listen to changes to parts of the state and are only able to dispatch certain actions.

## Local State

Some features have the need of adding some state to be handled by their reducer, but maybe that state is not necessary for the rest of the application. This is the case of the email and password texts in the onboarding reducer, for example. 

To deal with this kind of state we do the following:
- In the module's state use a public class with internal properties to store the needed local state
- We store that property in the global state. So that state in the end is part of the global state and it behaves the same way, but can only be accessed from the module that needs it.

This is the state in the Onboarding module
```kotlin
data class OnboardingState(
    val user: Loadable<User>,
    val localState: LocalState
) {
    data class LocalState internal constructor(
        internal val email: Email,
        internal val password: Password
    ) {
        constructor() : this(Email.Invalid(""), Password.Invalid(""))
    }
}
```

This is how it looks in the global app state
```kotlin
data class AppState(
    val workspaces: Map<Long, Workspace> = mapOf(),
    val onboardingLocalState: OnboardingState.LocalState = OnboardingState.LocalState(),
)
```

## High-order reducers

High-order reducers are basically reducers that take another reducer (and maybe also some other parameters). The outer reducer adds some behavior to the inner one, maybe transforming actions, stopping them or doing something with them before sending them forward to the inner reducer.

The simplest example of this the logging reducer, which for now just logs every dispatched action to the console:


```kotlin
class LoggingReducer(override val innerReducer: Reducer<AppState, AppAction>)
    : HigherOrderReducer<AppState, AppAction> {
    override fun reduce(
        state: SettableValue<AppState>,
        action: AppAction
    ): List<Effect<AppAction>> {
        Log.i(
            "LoggingReducer", when (action) {
                is AppAction.Onboarding -> action.onboarding.formatForDebug()
                is AppAction.Timer -> action.timer.formatForDebug()
                is AppAction.Loading -> action.loading.formatForDebug()
            }
        )

        return innerReducer.reduce(state, action)
    }
}
```

Analytics is something we will likely develop as a high-order reducer too.

## Features

Features are a part of the app that need their own Store view and its own reducer. The limits are blurry, but this usually means every time you present something modally, that's a new feature, but each one of the screens on that modal navigation are all part of the same feature.

Also, features can be nested: `Onboarding` is a feature, but it itself contains two other features: `EmailLogin` and `EmailSignup`. Similarly `Timer` is a feature but it also contains the `TimeEntriesLog` and the `StartEdit` features.

It's worth noticing that the `StartEdit` and `TimeEntriesLog` features present themselves as fragments inside the main `Timer` feature, so a feature not always indicates modality. Use your best judgment to think what it's worth of a feature status and what is just a view inside a feature. But basically it comes down to it having different responsibilities (and worth a different reducer and state)

## Project Structure

Our modules are created in a way that allows dependencies to be simple while still split in reasonable ways. 
The rules of thumb for adding new modules are:

- Bigger features that map directly into the app feature need their own modules
- Smaller features can be simple packages inside another module, if they are not big enough to justify a module
- All modules that don't need to depend on anything from the Android framework should be pure kotlin modules


Below is a diagram of our modules:

[![Android module overview](https://user-images.githubusercontent.com/7688727/78052157-16ca6a00-7355-11ea-8986-df9d403d0fbd.png)](https://user-images.githubusercontent.com/7688727/78052157-16ca6a00-7355-11ea-8986-df9d403d0fbd.png)
