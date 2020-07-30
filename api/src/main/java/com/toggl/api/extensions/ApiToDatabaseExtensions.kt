package com.toggl.api.extensions

import com.toggl.api.model.ApiClient
import com.toggl.api.model.ApiProject
import com.toggl.api.model.ApiTag
import com.toggl.api.model.ApiTask
import com.toggl.api.model.ApiTimeEntry
import com.toggl.api.model.ApiUser
import com.toggl.api.model.ApiWorkspace
import com.toggl.database.models.DatabaseClient
import com.toggl.database.models.DatabaseProject
import com.toggl.database.models.DatabaseTag
import com.toggl.database.models.DatabaseTask
import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseUser
import com.toggl.database.models.DatabaseWorkspace
import com.toggl.models.domain.WorkspaceFeature
import java.time.Duration
import java.time.OffsetDateTime

fun ApiClient.toDatabaseModel() = DatabaseClient(
    id,
    name,
    wid
)

fun ApiProject.toDatabaseModel() = DatabaseProject(
    id,
    name,
    "#4287f5",
    true,
    false,
    false,
    workspace_id,
    client_id
)

fun ApiTask.toDatabaseModel() = DatabaseTask(
    id,
    name,
    active,
    project_id,
    workspace_id,
    user_id
)

fun ApiTag.toDatabaseModel() = DatabaseTag(
    id,
    name,
    workspace_id
)

fun ApiTimeEntry.toDatabaseModel() = DatabaseTimeEntry(
    id,
    description,
    OffsetDateTime.now(),
    Duration.ofHours(1),
    false,
    2203288,
    null,
    null,
    false
)

fun ApiUser.toDatabaseModel() = DatabaseUser(
    id,
    api_token,
    "sdsd@asdasd.cz",
    "Tom",
    1
)

fun ApiWorkspace.toDatabaseModel() = DatabaseWorkspace(
    id,
    name,
    listOf(WorkspaceFeature.Pro)
)
