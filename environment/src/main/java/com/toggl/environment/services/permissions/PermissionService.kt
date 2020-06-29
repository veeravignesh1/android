package com.toggl.environment.services.permissions

interface PermissionService {
    fun checkCalendarPermission(): Boolean
    fun requestCalendarPermission()
}

fun PermissionService.requestCalendarPermissionIfNeeded() {
    if (checkCalendarPermission())
        return

    requestCalendarPermission()
}
