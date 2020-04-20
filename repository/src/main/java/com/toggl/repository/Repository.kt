package com.toggl.repository

import com.toggl.database.dao.ClientDao
import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TagDao
import com.toggl.database.dao.TaskDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.WorkspaceDao
import com.toggl.database.models.DatabaseClient
import com.toggl.database.models.DatabaseProject
import com.toggl.database.models.DatabaseTag
import com.toggl.database.models.DatabaseTask
import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseTimeEntryWithTags
import com.toggl.database.models.DatabaseWorkspace
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.WorkspaceFeature
import com.toggl.repository.dto.CreateProjectDTO
import com.toggl.repository.interfaces.ClientRepository
import com.toggl.repository.extensions.toDatabaseModel
import com.toggl.repository.extensions.toModel
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.repository.interfaces.StartTimeEntryResult
import com.toggl.repository.interfaces.TagRepository
import com.toggl.repository.interfaces.TaskRepository
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.repository.interfaces.WorkspaceRepository

class Repository(
    private val projectDao: ProjectDao,
    private val timeEntryDao: TimeEntryDao,
    private val workspaceDao: WorkspaceDao,
    private val clientDao: ClientDao,
    private val tagDao: TagDao,
    private val taskDao: TaskDao,
    private val timeService: TimeService
) : ProjectRepository, TimeEntryRepository, WorkspaceRepository, ClientRepository, TagRepository, TaskRepository {

    override suspend fun loadProjects(): List<Project> =
        projectDao.getAll().map(DatabaseProject::toModel)

    override suspend fun createProject(project: CreateProjectDTO): Project {
        val databaseProject = DatabaseProject(
            name = project.name,
            color = project.color,
            active = project.active,
            isPrivate = project.isPrivate,
            billable = project.billable,
            workspaceId = project.workspaceId,
            clientId = project.clientId
        )

        return projectDao.insert(databaseProject)
            .run(projectDao::getOne)
            .run(DatabaseProject::toModel)
    }

    override suspend fun loadTimeEntries(): List<TimeEntry> =
        timeEntryDao.getAllTimeEntriesWithTags().map(DatabaseTimeEntryWithTags::toModel)

    override suspend fun loadTags(): List<Tag> =
        tagDao.getAll().map(DatabaseTag::toModel)

    override suspend fun loadWorkspaces(): List<Workspace> =
        workspaceDao.getAll().let {
            if (it.any())
                return@let it.map(DatabaseWorkspace::toModel)

            // Automatically create a default workspace
            val workspace = DatabaseWorkspace(
                name = "Auto created workspace",
                features = listOf(WorkspaceFeature.Pro)
            )
            val workspaceId = workspaceDao.insert(workspace)

            return@let listOf(workspace.copy(id = workspaceId))
                .map(DatabaseWorkspace::toModel)
        }

    override suspend fun loadClients() = clientDao.getAll().map(DatabaseClient::toModel)

    override suspend fun loadTasks(): List<Task> = taskDao.getAll().map(DatabaseTask::toModel)

    override suspend fun startTimeEntry(
        workspaceId: Long,
        description: String
    ): StartTimeEntryResult {
        return timeEntryDao.startTimeEntry(workspaceId, description, timeService.now())
            .let { (started, stopped) ->
                StartTimeEntryResult(
                    started.let(DatabaseTimeEntry::toModel),
                    stopped.firstOrNull()?.let(DatabaseTimeEntry::toModel)
                )
            }
    }

    override suspend fun stopRunningTimeEntry(): TimeEntry? {
        val now = timeService.now()
        return timeEntryDao.stopRunningTimeEntries(now)
            .map(DatabaseTimeEntry::toModel)
            .firstOrNull()
    }

    override suspend fun editTimeEntry(timeEntry: TimeEntry): TimeEntry {
        return timeEntryDao.updateTimeEntryWithTags(timeEntry.toDatabaseModel()).run { timeEntry }
    }

    override suspend fun deleteTimeEntry(timeEntry: TimeEntry): TimeEntry {
        val timeEntryWithTags = timeEntry.copy(isDeleted = true).toDatabaseModel()
        return timeEntryWithTags
            .apply(timeEntryDao::updateTimeEntryWithTags)
            .let(DatabaseTimeEntryWithTags::toModel)
    }
}
