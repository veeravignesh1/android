package com.toggl.environment.di

import android.content.Context
import android.app.Activity
import android.os.Build
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.AppCenterAnalyticsService
import com.toggl.environment.services.analytics.CompositeAnalyticsService
import com.toggl.environment.services.analytics.FirebaseAnalyticsService
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.calendar.CursorCalendarService
import com.toggl.environment.services.permissions.LollipopPermissionService
import com.toggl.environment.services.permissions.MarshmallowPermissionService
import com.toggl.environment.services.permissions.PermissionService
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
}

@Module
@InstallIn(ActivityComponent::class)
object EnvironmentActivityModule {

    @Provides
    fun permissionService(activity: Activity): PermissionService =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) LollipopPermissionService()
        else MarshmallowPermissionService(activity)
}
