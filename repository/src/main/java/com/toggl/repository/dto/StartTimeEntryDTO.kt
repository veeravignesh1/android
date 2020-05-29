package com.toggl.repository.dto

import java.time.OffsetDateTime

data class StartTimeEntryDTO(
    val description: String,
    val startTime: OffsetDateTime,
    val billable: Boolean,
    val workspaceId: Long,
    val projectId: Long?,
    val taskId: Long?,
    val tagIds: List<Long>
)
