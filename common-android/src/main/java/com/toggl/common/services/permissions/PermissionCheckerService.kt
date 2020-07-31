package com.toggl.common.services.permissions

interface PermissionCheckerService {
    fun hasCalendarPermission(): Boolean
}