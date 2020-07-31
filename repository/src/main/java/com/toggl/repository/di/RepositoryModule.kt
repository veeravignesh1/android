package com.toggl.repository.di

import android.content.SharedPreferences
import com.toggl.api.ApiTokenProvider
import com.toggl.common.services.time.TimeService
import com.toggl.database.dao.ClientDao
import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TagDao
import com.toggl.database.dao.TaskDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.UserDao
import com.toggl.database.dao.WorkspaceDao
import com.toggl.repository.Repository
import com.toggl.repository.interfaces.AppRepository
import com.toggl.repository.interfaces.ClientRepository
import com.toggl.repository.interfaces.ProjectRepository
import com.toggl.repository.interfaces.SettingsRepository
import com.toggl.repository.interfaces.TagRepository
import com.toggl.repository.interfaces.TaskRepository
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.repository.interfaces.UserRepository
import com.toggl.repository.interfaces.WorkspaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun repository(
        projectDao: ProjectDao,
        timeEntryDao: TimeEntryDao,
        workspaceDao: WorkspaceDao,
        clientDao: ClientDao,
        tagDao: TagDao,
        taskDao: TaskDao,
        userDao: UserDao,
        sharedPreferences: SharedPreferences,
        timeService: TimeService
    ) = Repository(projectDao, timeEntryDao, workspaceDao, clientDao, tagDao, taskDao, userDao, sharedPreferences, timeService)

    @Provides
    @Singleton
    fun timeEntryRepository(repository: Repository): TimeEntryRepository =
        repository

    @Provides
    @Singleton
    fun workspaceRepository(repository: Repository): WorkspaceRepository =
        repository

    @Provides
    @Singleton
    fun projectRepository(repository: Repository): ProjectRepository =
        repository

    @Provides
    @Singleton
    fun clientRepository(repository: Repository): ClientRepository =
        repository

    @Provides
    @Singleton
    fun tagRepository(repository: Repository): TagRepository =
        repository

    @Provides
    @Singleton
    fun taskRepository(repository: Repository): TaskRepository =
        repository

    @Provides
    @Singleton
    fun settingsRepository(repository: Repository): SettingsRepository =
        repository

    @Provides
    @Singleton
    fun userRepository(repository: Repository): UserRepository =
        repository

    @Provides
    @Singleton
    fun appRepository(repository: Repository): AppRepository =
        repository

    @Provides
    @Singleton
    fun apiTokenProvider(repository: Repository): ApiTokenProvider = repository
}
