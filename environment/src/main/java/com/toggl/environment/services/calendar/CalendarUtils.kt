package com.toggl.environment.services.calendar

import android.Manifest
import android.content.Context
import androidx.core.content.PermissionChecker

fun Context.permissionToReadCalendarWasGranted(): Boolean =
    PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PermissionChecker.PERMISSION_GRANTED