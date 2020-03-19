package com.toggl.database

import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.WorkspaceDao

interface TogglDatabase {
    fun timeEntryDao(): TimeEntryDao
    fun projectDao(): ProjectDao
    fun workspaceDao(): WorkspaceDao
}
