package com.toggl.timer.startedit.domain

import com.toggl.architecture.extensions.noEffect
import com.toggl.timer.common.CoroutineTest
import com.toggl.timer.common.toMutableValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class BillableTappedActionTests : CoroutineTest() {
    val reducer = createReducer()

    @Test
    fun `The BillableTapped action should invert the values of the editable time entry`() {
        listOf(true, false).forEach { originalBillableValue ->
            var state = createInitialState().let {
                it.copy(
                    editableTimeEntry = it.editableTimeEntry.copy(billable = originalBillableValue)
                )
            }
            val mutableValue = state.toMutableValue { state = it }

            val effects = reducer.reduce(mutableValue, StartEditAction.BillableTapped)
            effects shouldBe noEffect()
            state.editableTimeEntry.billable shouldBe !originalBillableValue
        }
    }
}
