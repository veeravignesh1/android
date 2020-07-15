package com.toggl.di

import com.toggl.architecture.core.Subscription
import com.toggl.architecture.core.CompositeSubscription
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.loading.LoadProjectsSubscriptions
import com.toggl.domain.loading.LoadTimeEntriesSubscription
import com.toggl.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object SubscriptionModule {

    @Provides
    @ProvideAppSubscription
    fun compositeSubscription(
        @ProvideLoadTimeEntriesSubscription loadTimeEntriesSubscription: Subscription<AppState, AppAction>,
        @ProvideProjectsSubscription loadProjectsSubscription: Subscription<AppState, AppAction>
    ): Subscription<AppState, AppAction> =
        CompositeSubscription(listOf(
            loadTimeEntriesSubscription,
            loadProjectsSubscription
        ))

    @Provides
    @ProvideLoadTimeEntriesSubscription
    fun loadTimeEntriesSubscription(
        repository: Repository
    ): Subscription<AppState, AppAction> =
        LoadTimeEntriesSubscription(repository)

    @Provides
    @ProvideProjectsSubscription
    fun loadProjectSubscription(
        repository: Repository
    ): Subscription<AppState, AppAction> =
        LoadProjectsSubscriptions(repository)
}