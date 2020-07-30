package com.toggl.timer.startedit.domain

import com.toggl.architecture.core.Effect

class ReopenPickerEffect(val dateTimePickMode: DateTimePickMode) : Effect<StartEditAction> {
    override suspend fun execute(): StartEditAction? = StartEditAction.PickerTapped(dateTimePickMode)
}
