package com.toggl.common.services.time

import java.time.OffsetDateTime

class JavaTimeService : TimeService {
    override fun now(): OffsetDateTime =
        OffsetDateTime.now()
}
