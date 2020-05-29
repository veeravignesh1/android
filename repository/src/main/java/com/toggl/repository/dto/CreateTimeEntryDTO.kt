package com.toggl.repository.dto

import java.time.Duration
import java.time.OffsetDateTime

data class CreateTimeEntryDTO(
    val description: String,
    val startTime: OffsetDateTime,
    val duration: Duration,
    val billable: Boolean,
    val workspaceId: Long,
    val projectId: Long?,
    val taskId: Long?,
    val tagIds: List<Long>
)
