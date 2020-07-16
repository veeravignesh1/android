package com.toggl.common.services.permissions

interface PermissionRequesterService : PermissionCheckerService {
    suspend fun requestCalendarPermission(): Boolean
}

suspend fun PermissionRequesterService.requestCalendarPermissionIfNeeded(): Boolean =
    if (hasCalendarPermission()) true
    else requestCalendarPermission()
