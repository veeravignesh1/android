package com.toggl.repository.di

import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.WorkspaceDao
import com.toggl.environment.services.time.TimeService
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.repository.Repository
import com.toggl.repository.interfaces.WorkspaceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun repository(
        timeEntryDao: TimeEntryDao,
        workspaceDao: WorkspaceDao,
        timeService: TimeService
    ) = Repository(timeEntryDao, workspaceDao, timeService)

    @Provides
    @Singleton
    fun timeEntryRepository(repository: Repository): TimeEntryRepository =
        repository

    @Provides
    @Singleton
    fun workspaceRepository(repository: Repository): WorkspaceRepository =
        repository
}
