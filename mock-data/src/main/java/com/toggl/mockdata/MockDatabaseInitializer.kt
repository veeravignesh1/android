package com.toggl.mockdata

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.JsonObject
import com.squareup.moshi.Moshi
import com.toggl.api.extensions.toDatabaseModel
import com.toggl.api.model.ApiTimeEntry
import com.toggl.api.model.ApiTimeEntryJsonAdapter
import com.toggl.api.model.ApiUser
import com.toggl.api.model.ApiUserJsonAdapter
import com.toggl.common.services.time.TimeService
import com.toggl.database.dao.ClientDao
import com.toggl.database.dao.ProjectDao
import com.toggl.database.dao.TagDao
import com.toggl.database.dao.TaskDao
import com.toggl.database.dao.TimeEntryDao
import com.toggl.database.dao.UserDao
import com.toggl.database.dao.WorkspaceDao
import com.toggl.database.models.DatabaseTimeEntry
import com.toggl.database.models.DatabaseUser
import org.json.JSONArray
import java.io.IOException
import java.lang.IllegalStateException
import java.nio.charset.Charset

class MockDatabaseInitializer (
    private val context: Context,
    private val projectDao: ProjectDao,
    private val timeEntryDao: TimeEntryDao,
    private val workspaceDao: WorkspaceDao,
    private val clientDao: ClientDao,
    private val tagDao: TagDao,
    private val taskDao: TaskDao,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences,
    private val timeService: TimeService
) {
    suspend fun init() {

        val moshi = Moshi.Builder().build()

        val timeEntriesArray = JSONArray(parseFile("timeentries.txt"))
        // 99.9 = 2760
        // 99.5 = 1140
        // 99.0 = 866
        // 90.0 = 274
        // 80.0 = 162
        // median = 67
        val timeEntries: List<DatabaseTimeEntry> = (0 until 162)
            .map { timeEntriesArray.getJSONObject(it) }
            .map { ApiTimeEntryJsonAdapter(moshi).fromJson(it.toString()) }
            .map { it!!.toDatabaseModel() }
            .toList()
            .distinctBy { it.id }

        timeEntryDao.clear()
        timeEntryDao.insertAllTimeEntries(timeEntries)
    }

    suspend fun parseFile(filename: String): String {
        return try {
            val file = context.assets.open(filename)
            val size: Int = file.available()
            val buffer = ByteArray(size)
            file.read(buffer)
            file.close()
            String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            throw IllegalStateException("Mock data parsing fail")
        }
    }
}