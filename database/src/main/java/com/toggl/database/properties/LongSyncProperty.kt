package com.toggl.database.properties

data class LongSyncProperty(
    val current: Long,
    val backup: Long,
    val status: PropertySyncStatus
)
