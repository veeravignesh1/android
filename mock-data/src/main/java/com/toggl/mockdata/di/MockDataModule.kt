package com.toggl.mockdata.di

import android.content.Context
import android.content.SharedPreferences
import com.toggl.common.services.time.TimeService
import com.toggl.database.dao.ClientDao
import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TagDao
import com.toggl.database.dao.TaskDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.UserDao
import com.toggl.database.dao.WorkspaceDao
import com.toggl.mockdata.MockDatabaseInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class MockDataModule {
    @Provides
    @ActivityScoped
    fun mockDatabaseInitializer(
        @ActivityContext context: Context,
        projectDao: ProjectDao,
        timeEntryDao: TimeEntryDao,
        workspaceDao: WorkspaceDao,
        clientDao: ClientDao,
        tagDao: TagDao,
        taskDao: TaskDao,
        userDao: UserDao,
        sharedPreferences: SharedPreferences,
        timeService: TimeService
    ) = MockDatabaseInitializer(context, projectDao, timeEntryDao, workspaceDao, clientDao, tagDao, taskDao, userDao, sharedPreferences, timeService)
}