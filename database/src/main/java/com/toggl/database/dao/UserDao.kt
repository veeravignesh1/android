package com.toggl.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.toggl.database.models.DatabaseUser

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<DatabaseUser>

    @Insert
    fun insert(user: DatabaseUser)

    @Transaction
    fun set(databaseUser: DatabaseUser) {
        clear()
        insert(databaseUser)
    }

    @Query("DELETE FROM users")
    fun clear()
}
