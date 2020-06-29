package com.toggl.environment.services.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.M)
class MarshmallowPermissionService(private val activity: Activity) : PermissionService {
    private val calendarPermissionRequestCode: Int = 1234

    override fun checkCalendarPermission(): Boolean {
        val calendarPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
        return calendarPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun requestCalendarPermission() {
        activity.requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR), calendarPermissionRequestCode)
    }
}