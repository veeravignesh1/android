package com.toggl.environment.services.permissions

interface PermissionRequesterService : PermissionCheckerService {
    suspend fun requestCalendarPermission(): Boolean
}

suspend fun PermissionRequesterService.requestCalendarPermissionIfNeeded(): Boolean {
    if (hasCalendarPermission())
        return true

    return requestCalendarPermission()
}
