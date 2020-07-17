package com.toggl.database

import android.content.Context
import androidx.room.Room
import com.toggl.database.dao.ClientDao
import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TagDao
import com.toggl.database.dao.TaskDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.UserDao
import com.toggl.database.dao.WorkspaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context): TogglDatabase =
        Room.databaseBuilder(context, TogglRoomDatabase::class.java, "toggl.db").build()

    @Provides
    @Singleton
    fun timeEntryDao(appDatabase: TogglDatabase): TimeEntryDao = appDatabase.timeEntryDao()

    @Provides
    @Singleton
    fun workspaceDao(appDatabase: TogglDatabase): WorkspaceDao = appDatabase.workspaceDao()

    @Provides
    @Singleton
    fun projectDao(appDatabase: TogglDatabase): ProjectDao = appDatabase.projectDao()

    @Provides
    @Singleton
    fun clientDao(appDatabase: TogglDatabase): ClientDao = appDatabase.clientDao()

    @Provides
    @Singleton
    fun tagsDao(appDatabase: TogglDatabase): TagDao = appDatabase.tagDao()

    @Provides
    @Singleton
    fun taskDao(appDatabase: TogglDatabase): TaskDao = appDatabase.taskDao()

    @Provides
    @Singleton
    fun userDao(appDatabase: TogglDatabase): UserDao = appDatabase.userDao()
}
