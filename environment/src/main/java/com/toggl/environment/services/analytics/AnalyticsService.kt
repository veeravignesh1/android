package com.toggl.environment.services.analytics

interface AnalyticsService {
    fun track(event: Event)
}
