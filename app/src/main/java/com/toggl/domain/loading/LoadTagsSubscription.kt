package com.toggl.domain.loading

import com.toggl.architecture.DispatcherProvider
import com.toggl.repository.interfaces.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class LoadTagsSubscription(
    private val tagRepository: TagRepository,
    dispatcherProvider: DispatcherProvider
) : BaseLoadingSubscription(dispatcherProvider) {
    override fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction> {
        val projects = if (isUserLoggedIn) tagRepository.loadTags()
        else flowOf(emptyList())
        return projects.map { LoadingAction.TagsLoaded(it) }
    }
}
