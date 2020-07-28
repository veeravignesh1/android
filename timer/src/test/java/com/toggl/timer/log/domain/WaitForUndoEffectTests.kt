package com.toggl.timer.log.domain

@kotlinx.coroutines.ExperimentalCoroutinesApi
class WaitForUndoEffectTests
// FreeSpec({
//
//     "The WaitForUndoEffect" - {
//         "when executed, waits and then emits CommitDeletion" - {
//             val effect = WaitForUndoEffect(listOf(1, 8, 1337))
//
//             runBlockingTest {
//                 var action: TimeEntriesLogAction? = null
//
//                 launch {
//                     action = effect.execute()
//                 }
//
//                 advanceTimeBy(timeEntryDeletionDelayMs - 1)
//                 assertThat(action).isEqualTo(null)
//                 advanceTimeBy(1)
//                 assertThat(action).isEqualTo(TimeEntriesLogAction.CommitDeletion(listOf(1, 8, 1337)))
//             }
//         }
//     }
// })