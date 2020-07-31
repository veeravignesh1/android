package com.toggl.database.properties

import java.time.OffsetDateTime

data class NullableOffsetDateTimeSyncProperty(
    val current: OffsetDateTime?,
    val backup: OffsetDateTime?,
    val status: PropertySyncStatus
)
