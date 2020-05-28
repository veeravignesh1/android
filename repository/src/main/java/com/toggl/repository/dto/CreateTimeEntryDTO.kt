package com.toggl.repository.dto

import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

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
