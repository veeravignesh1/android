package com.toggl.common.services.permissions

import javax.inject.Inject

class LollipopPermissionRequesterService @Inject constructor() : PermissionRequesterService {
    override fun hasCalendarPermission() = true
    override suspend fun requestCalendarPermission() = true
}
