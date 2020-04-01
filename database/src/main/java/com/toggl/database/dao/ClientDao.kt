package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toggl.models.domain.Client

@Dao
interface ClientDao {
    @Query("SELECT * FROM Client")
    fun getAll(): List<Client>

    @Query("SELECT * FROM Client WHERE id = :id")
    fun getOne(id: Long): Client

    @Insert
    fun insertAll(vararg clients: Client): List<Long>

    @Insert
    fun insert(client: Client): Long

    @Update
    fun update(client: Client)

    @Update
    fun updateAll(clients: List<Client>)

    @Delete
    fun delete(client: Client)
}