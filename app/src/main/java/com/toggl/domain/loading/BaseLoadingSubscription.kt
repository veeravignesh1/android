package com.toggl.domain.loading

import com.toggl.architecture.Loadable
import com.toggl.architecture.core.Subscription
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
abstract class BaseLoadingSubscription : Subscription<AppState, AppAction> {
    override fun subscribe(state: Flow<AppState>): Flow<AppAction.Loading> =
        state.map { it.user is Loadable.Loaded }
            .distinctUntilChanged()
            .flatMapLatest { isLoggedIn -> subscribe(isLoggedIn).map { AppAction.Loading(it) } }

    abstract fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction>
}