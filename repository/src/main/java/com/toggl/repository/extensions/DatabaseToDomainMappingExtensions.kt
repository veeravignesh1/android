package com.toggl.repository.extensions

import com.toggl.database.models.DatabaseClient
import com.toggl.database.models.DatabaseProject
import com.toggl.database.models.DatabaseTag
import com.toggl.database.models.DatabaseTask
import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseTimeEntryWithTags
import com.toggl.database.models.DatabaseUser
import com.toggl.database.models.DatabaseWorkspace
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.User
import com.toggl.models.domain.Workspace
import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email

fun DatabaseTimeEntryWithTags.toModel() = TimeEntry(
    timeEntry.id,
    timeEntry.description,
    timeEntry.startTime,
    timeEntry.duration,
    timeEntry.billable,
    timeEntry.workspaceId,
    timeEntry.projectId,
    timeEntry.taskId,
    timeEntry.isDeleted,
    tags
)

fun DatabaseTimeEntry.toModelWithoutTags() = TimeEntry(
    id,
    description,
    startTime,
    duration,
    billable,
    workspaceId,
    projectId,
    taskId,
    isDeleted,
    emptyList()
)

fun DatabaseProject.toModel() = Project(
    id,
    name,
    color,
    active,
    isPrivate,
    billable,
    workspaceId,
    clientId
)

fun DatabaseTag.toModel() = Tag(
    id,
    name,
    workspaceId
)

fun DatabaseWorkspace.toModel() = Workspace(
    id,
    name,
    features
)

fun DatabaseClient.toModel() = Client(
    id,
    name,
    workspaceId
)

fun DatabaseTask.toModel() = Task(
    id,
    name,
    active,
    projectId,
    workspaceId,
    userId
)

fun DatabaseUser.toModel() = User(
    serverId,
    ApiToken.from(apiToken) as ApiToken.Valid,
    Email.from(email) as Email.Valid,
    name,
    defaultWorkspaceId
)
