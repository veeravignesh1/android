package com.toggl.timer.generators

import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.long

fun Arb.Companion.positiveLong(): Arb<Long> =
    Arb.long().filter { it > 0 }