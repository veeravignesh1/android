package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toggl.models.domain.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    fun getAll(): List<Project>

    @Query("SELECT * FROM Project WHERE id = :id")
    fun getOne(id: Long): Project

    @Insert
    fun insertAll(vararg projects: Project): List<Long>

    @Insert
    fun insert(project: Project): Long

    @Update
    fun update(project: Project)

    @Update
    fun updateAll(projects: List<Project>)

    @Delete
    fun delete(project: Project)
}