package com.toggl.common.feature.timeentry.extensions

import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeNewException
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeRunningException
import com.toggl.common.feature.timeentry.exceptions.TimeEntryShouldNotBeStoppedException
import com.toggl.models.domain.EditableTimeEntry

fun EditableTimeEntry.isNew() = this.startTime == null
fun EditableTimeEntry.isRunning() = this.duration == null
fun EditableTimeEntry.isRunningOrNew() = isRunning() || isNew()
fun EditableTimeEntry.isStopped() = this.startTime != null && this.duration != null

fun EditableTimeEntry.throwIfNew() {
    if (this.isNew()) {
        throw TimeEntryShouldNotBeNewException()
    }
}

fun EditableTimeEntry.throwIfRunning() {
    if (this.isRunning()) {
        throw TimeEntryShouldNotBeRunningException()
    }
}

fun EditableTimeEntry.throwIfStopped() {
    if (this.isStopped()) {
        throw TimeEntryShouldNotBeStoppedException()
    }
}