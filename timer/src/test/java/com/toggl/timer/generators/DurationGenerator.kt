package com.toggl.timer.generators

import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import java.time.Duration

fun Arb.Companion.threeTenDuration(): Arb<Duration> =
    positiveLong().map(Duration::ofMillis)