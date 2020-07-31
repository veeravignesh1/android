package com.toggl.api.network.models.reports

internal data class GraphItem(val seconds: Long, val byRate: Map<String, Long>)
