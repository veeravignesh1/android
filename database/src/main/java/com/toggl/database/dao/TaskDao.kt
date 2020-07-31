package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toggl.database.models.DatabaseTask

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<DatabaseTask>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getOne(id: Long): DatabaseTask

    @Insert
    fun insertAll(vararg tasks: DatabaseTask): List<Long>

    @Insert
    fun insert(task: DatabaseTask): Long

    @Update
    fun update(task: DatabaseTask)

    @Update
    fun updateAll(tasks: List<DatabaseTask>)

    @Delete
    fun delete(task: DatabaseTask)

    @Query("DELETE FROM tasks")
    fun clear()
}
