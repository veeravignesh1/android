package com.toggl.environment.services.permissions

class LollipopPermissionService : PermissionService {
    override fun checkCalendarPermission() = true
    override fun requestCalendarPermission() { }
}