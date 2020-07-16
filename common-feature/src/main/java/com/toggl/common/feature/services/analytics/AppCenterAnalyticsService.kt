package com.toggl.common.feature.services.analytics

import com.microsoft.appcenter.analytics.Analytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCenterAnalyticsService @Inject constructor() : AnalyticsService {
    override fun track(event: Event) =
        Analytics.trackEvent(event.name, event.parameters)
}
