package com.toggl.timer.startedit.domain

enum class TemporalInconsistency {
    None,
    StartTimeAfterCurrentTime,
    StartTimeAfterStopTime,
    StopTimeBeforeStartTime,
    DurationTooLong
}
