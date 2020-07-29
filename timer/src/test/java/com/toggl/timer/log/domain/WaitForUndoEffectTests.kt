package com.toggl.timer.log.domain

import com.toggl.common.Constants.timeEntryDeletionDelayMs
import com.toggl.timer.common.CoroutineTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
class WaitForUndoEffectTests : CoroutineTest() {

    @Test
    fun `The WaitForUndoEffect, when executed, waits and then emits CommitDeletion`() = runBlockingTest {
        val effect = WaitForUndoEffect(listOf(1, 8, 1337))

        var action: TimeEntriesLogAction? = null

        launch {
            action = effect.execute()
        }

        advanceTimeBy(timeEntryDeletionDelayMs - 1)
        action shouldBe null
        advanceTimeBy(1)
        action shouldBe TimeEntriesLogAction.CommitDeletion(listOf(1, 8, 1337))
    }
}
