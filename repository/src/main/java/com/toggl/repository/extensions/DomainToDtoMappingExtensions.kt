package com.toggl.repository.extensions

import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.repository.dto.CreateProjectDTO
import com.toggl.repository.dto.StartTimeEntryDTO
import org.threeten.bp.OffsetDateTime

fun EditableProject.toDto() = CreateProjectDTO(
    name,
    color,
    active,
    isPrivate,
    billable,
    workspaceId,
    clientId
)

fun EditableTimeEntry.toStartDto(defaultStartTime: OffsetDateTime) = StartTimeEntryDTO(
    description,
    startTime ?: defaultStartTime,
    billable,
    workspaceId,
    projectId,
    taskId,
    tagIds
)
