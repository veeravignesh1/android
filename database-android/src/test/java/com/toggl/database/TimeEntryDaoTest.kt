package com.toggl.database

import android.os.Build
import com.toggl.database.models.DatabaseTimeEntry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TimeEntryDaoTest {

    @Inject
    lateinit var database: TogglDatabase

    @Before
    fun setup() {
        DaggerTestComponent.builder()
            .build()
            .inject(this)

    }

    @Test
    fun testTesting() {
        val timeEntryDao = database.timeEntryDao()
        val stopped = timeEntryDao.getAllRunningTimeEntries()
        assertEquals(stopped, listOf<DatabaseTimeEntry>())
    }
}