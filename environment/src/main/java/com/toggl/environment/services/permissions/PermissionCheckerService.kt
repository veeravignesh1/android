package com.toggl.environment.services.permissions

interface PermissionCheckerService {
    fun hasCalendarPermission(): Boolean
}