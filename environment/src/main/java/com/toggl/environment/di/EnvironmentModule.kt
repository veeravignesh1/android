package com.toggl.environment.di

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.AppCenterAnalyticsService
import com.toggl.environment.services.analytics.CompositeAnalyticsService
import com.toggl.environment.services.analytics.FirebaseAnalyticsService
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.calendar.CursorCalendarService
import com.toggl.environment.services.permissions.LollipopPermissionRequesterService
import com.toggl.environment.services.permissions.MarshmallowPermissionRequesterService
import com.toggl.environment.services.permissions.PermissionCheckerService
import com.toggl.environment.services.permissions.PermissionRequesterService
import com.toggl.environment.services.time.JavaTimeService
import com.toggl.environment.services.time.TimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object EnvironmentModule {
    @Provides
    @Singleton
    fun timeService(): TimeService = JavaTimeService()

    @Provides
    @Singleton
    fun analyticsService(
        firebaseAnalyticsService: FirebaseAnalyticsService,
        appCenterAnalyticsService: AppCenterAnalyticsService
    ): AnalyticsService = CompositeAnalyticsService(firebaseAnalyticsService, appCenterAnalyticsService)

    @Provides
    @Singleton
    fun calendarService(@ApplicationContext context: Context): CalendarService = CursorCalendarService(context)

    @Provides
    @Singleton
    fun permissionCheckerService(@ApplicationContext context: Context): PermissionCheckerService =
        object : PermissionCheckerService {
            override fun hasCalendarPermission(): Boolean {
                val calendarPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                return calendarPermission == PackageManager.PERMISSION_GRANTED
            }
        }
}

@Module
@InstallIn(ActivityComponent::class)
object EnvironmentActivityModule {

    @Provides
    fun permissionRequesterService(activity: Activity): PermissionRequesterService =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) LollipopPermissionRequesterService()
        else MarshmallowPermissionRequesterService(activity as AppCompatActivity)
}
