package com.toggl.repository.extensions

import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseTimeEntryWithTags
import com.toggl.database.models.DatabaseUser
import com.toggl.models.domain.User
import com.toggl.repository.dto.CreateTimeEntryDTO
import com.toggl.repository.dto.StartTimeEntryDTO

fun CreateTimeEntryDTO.toDatabaseModel() = DatabaseTimeEntryWithTags(
    toDatabaseTimeEntry(),
    tagIds
)

fun StartTimeEntryDTO.toDatabaseModel() = DatabaseTimeEntryWithTags(
    toDatabaseTimeEntry(),
    tagIds
)

private fun CreateTimeEntryDTO.toDatabaseTimeEntry() = DatabaseTimeEntry.from(
    serverId = null,
    description = description,
    startTime = startTime,
    duration = duration,
    billable = billable,
    workspaceId = workspaceId,
    projectId = projectId,
    taskId = taskId,
    isDeleted = false
)

private fun StartTimeEntryDTO.toDatabaseTimeEntry() = DatabaseTimeEntry.from(
    serverId = null,
    description = description,
    startTime = startTime,
    duration = null,
    billable = billable,
    workspaceId = workspaceId,
    projectId = projectId,
    taskId = taskId,
    isDeleted = false
)

fun User.toDatabaseModel() = DatabaseUser(
    id,
    apiToken.toString(),
    email.toString(),
    name,
    defaultWorkspaceId
)
