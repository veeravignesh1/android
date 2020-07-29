package com.toggl.timer.extensions

import com.toggl.common.feature.extensions.formatForDisplaying
import com.toggl.timer.generators.threeTenDuration
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.MethodSource

@ExperimentalCoroutinesApi
class DurationFormatExtensionTests {

    @Test
    @MethodSource("durations")
    fun `The formatForDisplaying method uses the hh_mm_ss format`() = runBlockingTest {
        checkAll(Arb.threeTenDuration()) { duration ->
            val formatted = duration.formatForDisplaying()
            formatted.length shouldBeGreaterThanOrEqual 8
        }
    }
}