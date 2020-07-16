package com.toggl.domain.loading

import com.toggl.repository.interfaces.TimeEntryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoadTimeEntriesSubscription @Inject constructor(
    private val timeEntryRepository: TimeEntryRepository
) : BaseLoadingSubscription() {
    override fun subscribe(isUserLoggedIn: Boolean): Flow<LoadingAction> {
        val timeEntries = if (isUserLoggedIn) timeEntryRepository.loadTimeEntries()
        else flowOf(emptyList())
        return timeEntries.map { LoadingAction.TimeEntriesLoaded(it) }
    }
}