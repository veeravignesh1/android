package com.toggl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry

@Database(
    entities = [
        TimeEntry::class,
        Project::class
    ],
    version = 1
)
@TypeConverters(TogglTypeConverters::class)
abstract class TogglRoomDatabase : RoomDatabase(), TogglDatabase
