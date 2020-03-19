package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toggl.models.domain.Workspace

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM Workspace")
    fun getAll(): List<Workspace>

    @Query("SELECT * FROM Workspace WHERE id = :id")
    fun getOne(id: Long): Workspace

    @Insert
    fun insertAll(vararg workspaces: Workspace): List<Long>

    @Insert
    fun insert(workspace: Workspace): Long

    @Update
    fun update(workspace: Workspace)

    @Update
    fun updateAll(workspaces: List<Workspace>)

    @Delete
    fun delete(workspace: Workspace)
}