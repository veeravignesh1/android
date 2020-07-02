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

@RequiresApi(Build.VERSION_CODES.M)
class MarshmallowPermissionRequesterService @Inject constructor(private val activity: AppCompatActivity) : PermissionRequesterService {
    private val calendarPermissionRequestCode: Int = 1234

    override fun hasCalendarPermission(): Boolean {
        val calendarPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
        return calendarPermission == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestCalendarPermission(): Boolean =
        kotlin.coroutines.suspendCoroutine { cont ->
            val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                cont.resume(isGranted)
            }

            requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
        }
}