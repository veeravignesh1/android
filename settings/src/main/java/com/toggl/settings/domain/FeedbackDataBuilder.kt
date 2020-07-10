package com.toggl.settings.domain

import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.FeedbackData
import com.toggl.repository.Repository
import com.toggl.repository.interfaces.SettingsRepository
import javax.inject.Inject

class FeedbackDataBuilder @Inject constructor(
    private val repository: Repository,
    private val timeService: TimeService,
    private val settingsRepository: SettingsRepository
) {
    suspend fun assembleFeedbackData(): FeedbackData {
        val workspacesCount = repository.workspacesCount()
        val timeEntriesCount = repository.timeEntriesCount()
        val userPreferences = settingsRepository.loadUserPreferences()
        return FeedbackData(
            accountTimeZone = null, // TODO: actually get the user account timezone
            numberOfWorkspaces = workspacesCount,
            numberOfTimeEntries = timeEntriesCount,
            numberOfUnsyncedTimeEntries = Int.MIN_VALUE, // TODO: actually get number of unsynced time entries
            numberOfUnsyncableTimeEntries = Int.MIN_VALUE, // TODO: actually get number of unsyncable time entries
            lastSyncAttempt = null, // TODO: actually get time of the last attemped sync
            lastSuccessfulSync = null, // TODO: actually get time of the last successful sync
            deviceTime = timeService.now(),
            manualModeIsOn = userPreferences.manualModeEnabled,
            lastLogin = null // TODO: actually get the time of last login
        )
    }
}