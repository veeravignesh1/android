package com.toggl.environment.di

import android.content.Context
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.AppCenterAnalyticsService
import com.toggl.environment.services.analytics.CompositeAnalyticsService
import com.toggl.environment.services.analytics.FirebaseAnalyticsService
import com.toggl.environment.services.calendar.Calendar
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.time.JavaTimeService
import com.toggl.environment.services.time.TimeService
import dagger.Module
import dagger.Provides
import java.time.OffsetDateTime
import javax.inject.Singleton

@Module
class EnvironmentModule {
    @Provides
    @Singleton
    fun timeService(): TimeService =
        JavaTimeService()

    @Provides
    @Singleton
    fun analyticsService(
        firebaseAnalyticsService: FirebaseAnalyticsService,
        appCenterAnalyticsService: AppCenterAnalyticsService
    ): AnalyticsService =
        CompositeAnalyticsService(firebaseAnalyticsService, appCenterAnalyticsService)

    @Provides
    @Singleton
    fun firebaseAnalyticsService(context: Context) = FirebaseAnalyticsService(context)

    @Provides
    @Singleton
    fun appCenterAnalyticsService() = AppCenterAnalyticsService()

    @Provides
    @Singleton
    fun calendarService() = object : CalendarService {
        override fun getAvailableCalendars(): List<Calendar> {
            return emptyList()
        }

        override fun getCalendarEvents(
            fromStartDate: OffsetDateTime,
            toEndDate: OffsetDateTime,
            fromCalendars: List<Calendar>
        ): List<CalendarEvent> {
            return emptyList()
        }
    }
}
