package com.toggl.database.properties

data class NullableIntSyncProperty(
    val current: Int?,
    val backup: Int?,
    val status: PropertySyncStatus
)
