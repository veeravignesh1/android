package com.toggl.timer.common.domain

import com.google.common.truth.Truth.assertThat
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.timer.common.createTimeEntry

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.OffsetDateTime

@DisplayName("EditableTimeEntry")
internal class EditableTimeEntryTests {

    @Test
    fun `for groups, should sum durations of TEs in the represented group and have no start time`() {
        val timeEntries = listOf(
            createTimeEntry(1, duration = Duration.ofMinutes(3)),
            createTimeEntry(2, duration = Duration.ofMinutes(5)),
            createTimeEntry(3)
        )

        val ids = listOf<Long>(1, 2)
        val editable = EditableTimeEntry.fromGroup(timeEntries.filter { it.id in ids })

        assertThat(editable.duration).isEqualTo(Duration.ofMinutes(8))
        assertThat(editable.startTime).isEqualTo(null)
    }

    @Test
    fun `for single TEs, should copy over the duration and start time`() {
        val now = OffsetDateTime.now()
        val timeEntry = createTimeEntry(1, duration = Duration.ofMinutes(3), startTime = now)

        val editable = EditableTimeEntry.fromSingle(timeEntry)

        assertThat(editable.duration).isEqualTo(Duration.ofMinutes(3))
        assertThat(editable.startTime).isEqualTo(now)
    }

    @Test
    fun `for non-started TEs, initiates an empty editable`() {
        val editable = EditableTimeEntry.empty(1)

        assertThat(editable.duration).isEqualTo(null)
        assertThat(editable.startTime).isEqualTo(null)
        assertThat(editable.ids).isEmpty()
        assertThat(editable.description).isEqualTo("")
    }
}