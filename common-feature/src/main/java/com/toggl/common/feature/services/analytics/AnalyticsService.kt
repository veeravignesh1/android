package com.toggl.common.feature.services.analytics

interface AnalyticsService {
    fun track(event: Event)
}
