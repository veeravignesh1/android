package com.toggl.models.domain

import java.time.OffsetDateTime
import java.util.TimeZone

data class FeedbackData(
    val accountTimeZone: TimeZone?,
    val numberOfWorkspaces: Int,
    val numberOfTimeEntries: Int,
    val numberOfUnsyncedTimeEntries: Int,
    val numberOfUnsyncableTimeEntries: Int,
    val lastSyncAttempt: OffsetDateTime?,
    val lastSuccessfulSync: OffsetDateTime?,
    val deviceTime: OffsetDateTime,
    val manualModeIsOn: Boolean,
    val lastLogin: OffsetDateTime?
)
