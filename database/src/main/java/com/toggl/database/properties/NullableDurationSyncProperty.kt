package com.toggl.database.properties

import java.time.Duration

data class NullableDurationSyncProperty(
    val current: Duration?,
    val backup: Duration?,
    val status: PropertySyncStatus
)
