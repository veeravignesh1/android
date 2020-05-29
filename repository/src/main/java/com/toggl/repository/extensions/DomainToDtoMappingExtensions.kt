package com.toggl.repository.extensions

import com.toggl.models.domain.EditableProject
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.repository.dto.CreateProjectDTO
import com.toggl.repository.dto.CreateTimeEntryDTO
import com.toggl.repository.dto.StartTimeEntryDTO
import com.toggl.repository.exceptions.DurationShouldNotBeNullException
import com.toggl.repository.exceptions.StartTimeShouldNotBeNullException
import java.time.OffsetDateTime

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

fun EditableTimeEntry.toCreateDto() = CreateTimeEntryDTO(
    description,
    startTime ?: throw StartTimeShouldNotBeNullException(),
    duration ?: throw DurationShouldNotBeNullException(),
    billable,
    workspaceId,
    projectId,
    taskId,
    tagIds
)
