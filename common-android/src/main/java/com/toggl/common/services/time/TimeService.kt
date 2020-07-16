package com.toggl.common.services.time

import java.time.OffsetDateTime

interface TimeService {
    fun now(): OffsetDateTime
}
