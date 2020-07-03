package com.toggl.environment.services.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.M)
class MarshmallowPermissionRequesterService @Inject constructor(private val activity: AppCompatActivity) : PermissionRequesterService {

    override fun hasCalendarPermission(): Boolean {
        val calendarPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
        return calendarPermission == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestCalendarPermission(): Boolean =
        suspendCoroutine { continuation ->
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission(), continuation::resume)
                .launch(Manifest.permission.READ_CALENDAR)
        }
}