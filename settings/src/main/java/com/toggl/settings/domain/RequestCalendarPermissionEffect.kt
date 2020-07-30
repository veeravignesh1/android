package com.toggl.settings.domain

import com.toggl.architecture.core.Effect

class RequestCalendarPermissionEffect : Effect<SettingsAction.CalendarPermissionRequested> {
    override suspend fun execute(): SettingsAction.CalendarPermissionRequested = SettingsAction.CalendarPermissionRequested
}
